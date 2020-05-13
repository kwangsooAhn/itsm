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
import java.nio.file.Paths
import java.time.LocalDateTime
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class FormService(private val restTemplate: RestTemplateProvider) {

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
        val urlDto = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Form.PUT_FORM_DATA.url.replace(restTemplate.getKeyRegex(), formId)
        )
        val responseEntity = restTemplate.update(urlDto, formComponentSaveDto)
        return responseEntity.body.toString().isNotEmpty()
    }

    fun saveAsForm(formData: String): String {
        val formComponentSaveDto = makeFormComponentSaveDto(formData)
        if (formComponentSaveDto.form.status == RestTemplateConstants.FormStatus.DESTROY.value) {
            formComponentSaveDto.form.status = RestTemplateConstants.FormStatus.EDIT.value
        }
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

    fun getFormImageList(): String {
        val absolutePath = Paths.get("").toAbsolutePath().toString()
        var dir = Paths.get(absolutePath, RestTemplateConstants.RESOURCES_DIR, RestTemplateConstants.FORM_IMAGE_DIR)
        dir = if (Files.exists(dir)) dir else Files.createDirectories(dir)
        val fileList = JsonArray()
        Files.walk(dir)
            .filter { Files.isRegularFile(it) }
            //.filter { it -> it.toString().endsWith(".jpg") }  //TODO: 이미지만 필터 ([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)
            .forEach {
                val fileJson = JsonObject()
                fileJson.addProperty("fname", it.fileName.toString())
                fileJson.addProperty(
                    "imgPath",
                    Paths.get(RestTemplateConstants.RESOURCES_DIR).toUri().relativize(it.toUri()).toString()
                )   //상대 경로
                fileJson.addProperty("imgUrl", it.toUri().toURL().toString())
                fileJson.addProperty("fsize", it.toFile().length())
                fileList.add(fileJson)
            }
        return fileList.toString()
    }
}
