/**
 * ITSM 폼 관련 서비스
 *
 * 폼 관련 핵심 기능은 Workflow 패키지에서 구현하고 여기서는 ITSM 레벨에서 필요한 서비스를 구현.
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.form.service

import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.form.dto.FormSearchCondition
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateFormDataDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.dto.RestTemplateFormListReturnDto
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FormService(
    private val wfFormService: WfFormService,
    private val currentSessionUser: CurrentSessionUser
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 폼 목록 조회.
     *
     * @param formSearchCondition 검색조건을 포함하는 Map
     * @return List<RestTemplateFormDto>
     */
    fun findForms(formSearchCondition: FormSearchCondition): RestTemplateFormListReturnDto {
        return wfFormService.getFormList(formSearchCondition)
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
     * @param formData
     * @return String 생성된 form ID
     */
    fun createForm(formData: RestTemplateFormDataDto): String {
        formData.status = RestTemplateConstants.FormStatus.EDIT.value
        formData.createUserKey = currentSessionUser.getUserKey()
        formData.createDt = LocalDateTime.now()
        formData.updateDt = LocalDateTime.now()
        val newFormId = wfFormService.createForm(RestTemplateFormDto(
            name = formData.name,
            status = formData.status,
            desc = formData.desc
        )).id
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
     * @param formDataDto
     * @return Boolean
     */
    fun saveFormData(formId: String, formDataDto: RestTemplateFormDataDto): Boolean {
        formDataDto.updateDt = LocalDateTime.now()
        formDataDto.updateUserKey = currentSessionUser.getUserKey()
        logger.info("save form : [{}]", formId)
        return wfFormService.saveFormData(formDataDto)
    }

    /**
     * 폼 새이름으로 저장하기
     *
     * @param formData
     * @return String 새로 생성된 form ID
     */
    fun saveAsForm(formData: RestTemplateFormDataDto): String {
        formData.status = RestTemplateConstants.FormStatus.EDIT.value
        val newFormId = wfFormService.saveAsFormData(formData).id
        logger.info("save as form : success [{}]", newFormId)
        return newFormId
    }
}
