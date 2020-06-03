package co.brainz.itsm.form.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.util.AliceTimezoneUtils
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateFormComponentSaveDto
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
        val formComponentSaveDto = makeFormComponentSaveDto(formData)
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        formComponentSaveDto.form.updateDt = AliceTimezoneUtils().toGMT(LocalDateTime.now())
        formComponentSaveDto.form.updateUserKey = aliceUserDto.userKey
        val urlDto = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Form.PUT_FORM_DATA.url.replace(restTemplate.getKeyRegex(), formId)
        )
        val responseEntity = restTemplate.update(urlDto, formComponentSaveDto)
        return responseEntity.body.toString().isNotEmpty()
    }

    fun saveAsForm(formData: String): String {
        val formComponentSaveDto = makeFormComponentSaveDto(formData)
        formComponentSaveDto.form.status = RestTemplateConstants.FormStatus.EDIT.value

        val urlDto = RestTemplateUrlDto(callUrl = RestTemplateConstants.Form.POST_FORM_SAVE_AS.url)
        val responseEntity = restTemplate.createToSave(urlDto, formComponentSaveDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> {
                val dataDto = mapper.readValue(responseEntity.body.toString(), RestTemplateFormDto::class.java)
                dataDto.id
            }
            false -> ""
        }
    }

    fun makeFormComponentSaveDto(formData: String): RestTemplateFormComponentSaveDto {
        val map = mapper.readValue(formData, LinkedHashMap::class.java)
        val forms = mapper.convertValue(map["form"], RestTemplateFormDto::class.java)
        val components: MutableList<LinkedHashMap<String, Any>> = mapper.convertValue(
            map["components"],
            TypeFactory.defaultInstance().constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
        )

        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val formSaveDto = RestTemplateFormDto(
            id = forms.id,
            name = forms.name,
            desc = forms.desc,
            status = forms.status,
            createDt = AliceTimezoneUtils().toGMT(LocalDateTime.now()),
            createUserKey = aliceUserDto.userKey
        )
        return RestTemplateFormComponentSaveDto(
            form = formSaveDto,
            components = components
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
            .filter { it -> it.fileName.toString().matches(imageRegex) }
            .forEach {
                val fileJson = JsonObject()
                fileJson.addProperty("fileName", it.fileName.toString())
                val relativePath = ClassPathResource(RestTemplateConstants.BASE_DIR).uri.relativize(it.toUri())
                fileJson.addProperty("imgPath", "/$relativePath") // 상대 경로 /asset/...
                fileJson.addProperty("imgUrl", it.toUri().toURL().toString()) // file://...
                fileJson.addProperty("fileSize", it.toFile().length())
                fileList.add(fileJson)
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
            val fileJson = JsonObject()
            fileJson.addProperty("fileName", destDir.fileName.toString())
            val relativePath = ClassPathResource(RestTemplateConstants.BASE_DIR).uri.relativize(destDir.toUri())
            fileJson.addProperty("imgPath", "/$relativePath")
            fileJson.addProperty("imgUrl", destDir.toUri().toURL().toString())
            fileJson.addProperty("fileSize", destDir.toFile().length())
            rtn = fileJson.toString()
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
}
