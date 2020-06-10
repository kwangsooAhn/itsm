package co.brainz.itsm.form.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.ComponentDetail
import co.brainz.workflow.provider.dto.RestTemplateFormComponentListDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.multipart.MultipartFile

@Service
class FormService(private val restTemplate: RestTemplateProvider) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun findForms(params: LinkedMultiValueMap<String, String>): List<RestTemplateFormDto> {
        val urlDto = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.GET_FORMS.url, parameters = params)
        val responseBody = restTemplate.get(urlDto)
        val forms: List<RestTemplateFormDto> = mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateFormDto::class.java)
        )
        for (form in forms) {
            form.createDt = form.createDt?.let { AliceTimezoneUtils().toTimezone(it) }
            form.updateDt = form.updateDt?.let { AliceTimezoneUtils().toTimezone(it) }
        }

        return forms
    }

    fun getFormData(formId: String): String {
        val urlDto = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Form.GET_FORM_DATA.url.replace(
                restTemplate.getKeyRegex(),
                formId
            )
        )
        return restTemplate.get(urlDto)
    }

    fun createForm(restTemplateFormDto: RestTemplateFormDto): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateFormDto.status = RestTemplateConstants.FormStatus.EDIT.value
        restTemplateFormDto.createUserKey = aliceUserDto.userKey
        restTemplateFormDto.createDt = AliceTimezoneUtils().toGMT(LocalDateTime.now())
        restTemplateFormDto.updateDt = restTemplateFormDto.updateDt?.let { AliceTimezoneUtils().toGMT(it) }
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.POST_FORM.url)
        val responseEntity = restTemplate.create(url, restTemplateFormDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> {
                val dataDto = mapper.readValue(responseEntity.body.toString(), RestTemplateFormDto::class.java)
                dataDto.id
            }
            false -> ""
        }
    }

    fun deleteForm(formId: String): ResponseEntity<String> {
        val urlDto = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Form.DELETE_FORM.url.replace(
                restTemplate.getKeyRegex(),
                formId
            )
        )
        return restTemplate.delete(urlDto)
    }

    fun saveFormData(formId: String, formData: String): Boolean {
        val formComponentListDto = makeFormComponentListDto(formData)
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        formComponentListDto.updateDt = AliceTimezoneUtils().toGMT(LocalDateTime.now())
        formComponentListDto.updateUserKey = aliceUserDto.userKey
        val urlDto = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Form.PUT_FORM_DATA.url.replace(restTemplate.getKeyRegex(), formId)
        )
        val responseEntity = restTemplate.update(urlDto, formComponentListDto)
        return responseEntity.body.toString().isNotEmpty()
    }

    fun saveAsForm(formData: String): String {
        val formComponentListDto = makeFormComponentListDto(formData)
        formComponentListDto.status = RestTemplateConstants.FormStatus.EDIT.value

        val urlDto = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.POST_FORM_SAVE_AS.url)
        val responseEntity = restTemplate.createToSave(urlDto, formComponentListDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> {
                val dataDto = mapper.readValue(responseEntity.body.toString(), RestTemplateFormDto::class.java)
                dataDto.id
            }
            false -> ""
        }
    }

    fun makeFormComponentListDto(formData: String): RestTemplateFormComponentListDto {
        val map = mapper.readValue(formData, LinkedHashMap::class.java)
        val components: MutableList<LinkedHashMap<String, Any>> = mapper.convertValue(
            map["components"],
            TypeFactory.defaultInstance().constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
        )

        val linkedMapType = TypeFactory.defaultInstance()
            .constructMapType(LinkedHashMap::class.java, String::class.java, Any::class.java)
        val componentDetailList: MutableList<ComponentDetail> = mutableListOf()
        for (component in components) {
            var values: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
            component["values"]?.let {
                values = mapper.convertValue(
                    it, TypeFactory.defaultInstance()
                        .constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
                )
            }

            var dataAttribute: LinkedHashMap<String, Any> = linkedMapOf()
            component["dataAttribute"]?.let {
                dataAttribute =
                    mapper.convertValue(
                        component["dataAttribute"],
                        linkedMapType
                    )
            }

            var display: LinkedHashMap<String, Any> = linkedMapOf()
            component["display"]?.let {
                display =
                    mapper.convertValue(component["display"], linkedMapType)
            }

            var label: LinkedHashMap<String, Any> = linkedMapOf()
            component["label"]?.let {
                label = mapper.convertValue(component["label"], linkedMapType)
            }

            var validate: LinkedHashMap<String, Any> = linkedMapOf()
            component["validate"]?.let {
                validate =
                    mapper.convertValue(component["validate"], linkedMapType)
            }

            var option: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
            component["option"]?.let {
                option = mapper.convertValue(
                    it, TypeFactory.defaultInstance()
                        .constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
                )
            }

            componentDetailList.add(
                ComponentDetail(
                    componentId = component["componentId"] as String,
                    type = component["type"] as String,
                    values = values,
                    dataAttribute = dataAttribute,
                    display = display,
                    label = label,
                    validate = validate,
                    option = option
                )
            )
        }

        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        return RestTemplateFormComponentListDto(
            formId = map["formId"] as String,
            name = map["name"] as String,
            desc = map["desc"] as String,
            status = map["status"] as String,
            createDt = AliceTimezoneUtils().toGMT(LocalDateTime.now()),
            createUserKey = aliceUserDto.userKey,
            updateDt = null,
            updateUserKey = null,
            components = componentDetailList
        )
    }

    /**
     * 이미지 업로드 대상 파일 경로 구하기
     *
     * @param rootDir 업로드할 경로
     */
    private fun getImageBaseDir(rootDir: String): Path {
        val basePath = ClassPathResource(RestTemplateConstants.BASE_DIR).file.path.toString()
        var dir = Paths.get(basePath, rootDir)
        dir = if (Files.exists(dir)) dir else Files.createDirectories(dir)
        return dir
    }

    fun getFormImageList(): String {
        val dir = getImageBaseDir(RestTemplateConstants.FORM_IMAGE_DIR)
        val fileList = JsonArray()
        val imageRegex = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))\$)".toRegex()
        Files.walk(dir)
            .filter { Files.isRegularFile(it) }
            .filter { it.fileName.toString().matches(imageRegex) }
            .forEach {
                fileList.add(this.setFileJson(it))
            }
        return fileList.toString()
    }

    fun uploadFile(multipartFile: MultipartFile): String {
        var rtn = ""
        val dir = getImageBaseDir(RestTemplateConstants.FORM_IMAGE_DIR)
        val destDir = Paths.get(dir.toString(), multipartFile.originalFilename)
        try {
            multipartFile.transferTo(destDir.toFile())
            // 파일 저장 후 경로를 담아서 전달한다.
            rtn = this.setFileJson(destDir).toString()
        } catch (e: Exception) {
            logger.error("File upload failed.")
            logger.error("{}", e.message)
        }
        return rtn
    }

    fun deleteFile(jsonData: String): Boolean {
        var rtn = false
        val map = mapper.readValue(jsonData, LinkedHashMap::class.java)
        val dir = getImageBaseDir(RestTemplateConstants.FORM_IMAGE_DIR)
        val delFile = Paths.get(dir.toString(), map["name"].toString())
        if (Files.exists(delFile)) {
            Files.delete(delFile)
            rtn = true
        }
        return rtn
    }

    private fun setFileJson(path: Path): JsonObject {
        val fileJson = JsonObject()
        fileJson.addProperty("fileName", path.fileName.toString())
        val relativePath = ClassPathResource(RestTemplateConstants.BASE_DIR).uri.relativize(path.toUri())
        fileJson.addProperty("imgPath", "/$relativePath") // 상대 경로 /asset/...
        fileJson.addProperty("imgUrl", path.toUri().toURL().toString()) // file://...
        fileJson.addProperty("fileSize", path.toFile().length())
        return fileJson
    }
}
