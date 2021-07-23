/**
 * ITSM 폼 관련 서비스
 *
 * 폼 관련 핵심 기능은 Workflow 패키지에서 구현하고 여기서는 ITSM 레벨에서 필요한 서비스를 구현.
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.form.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.util.AliceUtil
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.FormComponentDto
import co.brainz.workflow.provider.dto.FormGroupDto
import co.brainz.workflow.provider.dto.FormRowDto
import co.brainz.workflow.provider.dto.RestTemplateFormDataDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FormService(
    private val wfFormService: WfFormService,
    private val currentSessionUser: CurrentSessionUser
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 폼 목록 조회.
     *
     * @param params 검색조건을 포함하는 Map
     * @return List<RestTemplateFormDto>
     */
    fun findForms(params: LinkedHashMap<String, Any>): List<RestTemplateFormDto> {
        return wfFormService.getFormList(params)
    }

    /**
     * 폼 마스터 정보 조회
     *
     * @param formId
     * @return RestTemplateFormDto
     */
    fun getForm(formId: String): RestTemplateFormDto {
        return wfFormService.getFormInfo(formId)
    }

    /**
     * 폼 1개 마스터 데이터 업데이트
     *
     * @param formId
     * @param restTemplateFormDto
     * @return Boolean
     */
    fun saveForm(formId: String, restTemplateFormDto: RestTemplateFormDto): Boolean {
        restTemplateFormDto.updateDt = LocalDateTime.now()
        restTemplateFormDto.updateUserKey = currentSessionUser.getUserKey()

        logger.info("update form : [{}]", formId)
        return wfFormService.saveForm(restTemplateFormDto)
    }

    /**
     * 폼 전체 정보 조회
     *
     * 폼 마스터 데이터와 폼에 포함되는 Group, Row, Component 를 모두 포함하는 데이터 조회
     *
     * @param formId
     * @return RestTemplateFormDataDto
     */
    fun getFormData(formId: String): RestTemplateFormDataDto {
        return wfFormService.getFormData(formId)
    }

    /**
     * 폼 생성
     *
     * @param restTemplateFormDto
     * @return String 생성된 form ID
     */
    fun createForm(restTemplateFormDto: RestTemplateFormDto): String {
        restTemplateFormDto.status = RestTemplateConstants.FormStatus.EDIT.value
        restTemplateFormDto.createUserKey = currentSessionUser.getUserKey()
        restTemplateFormDto.createDt = LocalDateTime.now()
        restTemplateFormDto.updateDt = LocalDateTime.now()
        val newFormId = wfFormService.createForm(restTemplateFormDto).id
        logger.info("create form : success [{}]", newFormId)
        return newFormId
    }

    /**
     * 폼 삭제
     *
     * @param formId
     * @return Boolean
     */
    fun deleteForm(formId: String): Boolean {
        logger.info("delete form : [{}]", formId)
        return wfFormService.deleteForm(formId)
    }

    /**
     * 폼 전체 데이터 저장
     *
     * 폼과 폼에 속한 Group, Row, Component 전체 구조를 저장.
     *
     * @param formId
     * @param formDataString
     * @return Boolean
     */
    fun saveFormData(formId: String, formDataString: String): Boolean {
        val formDataDto = makeFormDataDto(formDataString)
        formDataDto.updateDt = LocalDateTime.now()
        formDataDto.updateUserKey = currentSessionUser.getUserKey()
        logger.info("save form : [{}]", formId)
        return wfFormService.saveFormData(formDataDto)
    }

    /**
     * 폼 새이름으로 저장하기
     *
     * @param formDataString
     * @return String 새로 생성된 form ID
     */
    fun saveAsForm(formDataString: String): String {
        val formDataDto = makeFormDataDto(formDataString)
        formDataDto.status = RestTemplateConstants.FormStatus.EDIT.value
        val newFormId = wfFormService.saveAsFormData(formDataDto).id
        logger.info("save as form : success [{}]", newFormId)
        return newFormId
    }

    /**
     * 화면에서 폼 데이터(String)를 FormDataDto 포맷으로 변환
     *
     * @param formDataString
     * @return RestTemplateFormDataDto
     */
    private fun makeFormDataDto(formDataString: String): RestTemplateFormDataDto {
        val map = mapper.readValue(formDataString, LinkedHashMap::class.java)
        val linkedMapType = TypeFactory.defaultInstance()
            .constructMapType(LinkedHashMap::class.java, String::class.java, Any::class.java)

        // Form display option
        var formDisplay: LinkedHashMap<String, Any> = linkedMapOf()
        map["display"]?.let {
            formDisplay = mapper.convertValue(map["display"], linkedMapType)
        }

        // Group list in form
        val formGroupList: MutableList<LinkedHashMap<String, Any>> = mapper.convertValue(
            map["group"],
            TypeFactory.defaultInstance().constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
        )
        val groupList: MutableList<FormGroupDto> = mutableListOf()
        for (formGroup in formGroupList) {
            val groupDisplay = AliceUtil().convertStringToLinkedHashMap(formGroup["display"])
            val groupLabel = AliceUtil().convertStringToLinkedHashMap(formGroup["label"])

            // Row list in group
            val rowListInGroup: MutableList<LinkedHashMap<String, Any>> = mapper.convertValue(
                formGroup["row"],
                TypeFactory.defaultInstance()
                    .constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
            )
            val formRowList: MutableList<FormRowDto> = mutableListOf()
            for (rowInGroup in rowListInGroup) {
                val rowDisplay = AliceUtil().convertStringToLinkedHashMap(rowInGroup["display"])
                val rowComponentList: MutableList<LinkedHashMap<String, Any>> = mapper.convertValue(
                    rowInGroup["component"],
                    TypeFactory.defaultInstance()
                        .constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
                )

                val formComponentList: MutableList<FormComponentDto> = mutableListOf()
                for (component in rowComponentList) {
                    val componentDisplay = AliceUtil().convertStringToLinkedHashMap(component["display"])
                    val componentLabel = AliceUtil().convertStringToLinkedHashMap(component["label"])
                    val componentValidation = AliceUtil().convertStringToLinkedHashMap(component["validation"])
                    val componentElement = AliceUtil().convertStringToLinkedHashMap(component["element"])

                    formComponentList.add(
                        @Suppress("UNCHECKED_CAST")
                        FormComponentDto(
                            id = component["id"] as String,
                            type = component["type"] as String,
                            isTopic = component["isTopic"] as Boolean,
                            tags = component["tags"] as List<AliceTagDto>,
                            value = component["value"] as String,
                            display = componentDisplay,
                            label = componentLabel,
                            validation = componentValidation,
                            element = componentElement
                        )
                    )
                }

                formRowList.add(
                    FormRowDto(
                        id = rowInGroup["id"] as String,
                        display = rowDisplay,
                        component = formComponentList
                    )
                )
            }

            groupList.add(
                FormGroupDto(
                    id = formGroup["id"] as String,
                    name = formGroup["name"] as String,
                    display = groupDisplay,
                    label = groupLabel,
                    row = formRowList
                )
            )
        }

        return RestTemplateFormDataDto(
            id = (map["id"] ?: "") as String,
            name = map["name"] as String,
            desc = map["desc"] as String,
            status = (map["status"] ?: "") as String,
            category = map["category"] as String,
            display = formDisplay,
            createDt = LocalDateTime.now(),
            createUserKey = currentSessionUser.getUserKey(),
            updateDt = null,
            updateUserKey = null,
            group = groupList
        )
    }
}
