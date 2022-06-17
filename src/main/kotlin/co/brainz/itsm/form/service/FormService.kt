/**
 * ITSM 폼 관련 서비스
 *
 * 폼 관련 핵심 기능은 Workflow 패키지에서 구현하고 여기서는 ITSM 레벨에서 필요한 서비스를 구현.
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.form.service

import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.form.dto.FormSearchCondition
import co.brainz.workflow.component.dto.WfComponentTemplateDto
import co.brainz.workflow.component.service.WfComponentTemplateService
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.provider.constants.WorkflowConstants
import co.brainz.workflow.provider.dto.RestTemplateFormDataDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.dto.RestTemplateFormListReturnDto
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

@Service
class FormService(
    private val wfFormService: WfFormService,
    private val wfComponentTemplateService: WfComponentTemplateService,
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
     */
    fun saveForm(formId: String, restTemplateFormDto: RestTemplateFormDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        when (wfFormService.checkFormData(formId = formId, formName = restTemplateFormDto.name)) {
            WfFormConstants.Status.STATUS_ERROR_DUPLICATE_FORM_NAME.code -> {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
            else -> {
                restTemplateFormDto.updateDt = LocalDateTime.now()
                restTemplateFormDto.updateUserKey = currentSessionUser.getUserKey()

                logger.info("update form : [{}]", formId)
                wfFormService.saveForm(restTemplateFormDto)
            }
        }
        return ZResponse(
            status = status.code
        )
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
     */
    fun createForm(formData: RestTemplateFormDataDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        var formMap: Map<String, String>? = null
        when (wfFormService.checkFormData(formId = null, formName = formData.name)) {
            WfFormConstants.Status.STATUS_ERROR_DUPLICATE_FORM_NAME.code -> {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
            else -> {
                val newFormId = wfFormService.createForm(
                    RestTemplateFormDto(
                        name = formData.name,
                        status = WorkflowConstants.FormStatus.EDIT.value,
                        desc = formData.desc,
                        editable = true,
                        createUserKey = currentSessionUser.getUserKey(),
                        createDt = LocalDateTime.now(),
                        updateDt = LocalDateTime.now()
                    )
                ).id
                logger.info("create form : success [{}]", newFormId)
                formMap = mutableMapOf()
                formMap["formId"] = newFormId
            }
        }
        return ZResponse(
            status = status.code,
            data = formMap
        )
    }

    /**
     * 폼 삭제
     *
     * @param formId
     */
    fun deleteForm(formId: String): ZResponse {
        logger.info("delete form : [{}]", formId)
        val status = if (wfFormService.deleteForm(formId)) {
            ZResponseConstants.STATUS.SUCCESS
        } else {
            ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 폼 전체 데이터 저장
     *
     * 폼과 폼에 속한 Group, Row, Component 전체 구조를 저장.
     *
     * @param formId
     * @param formDataDto
     */
    fun saveFormData(formId: String, formDataDto: RestTemplateFormDataDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        when (wfFormService.checkFormData(formId = formId, formName = formDataDto.name)) {
            WfFormConstants.Status.STATUS_ERROR_DUPLICATE_FORM_NAME.code -> {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
            else -> {
                formDataDto.updateDt = LocalDateTime.now()
                formDataDto.updateUserKey = currentSessionUser.getUserKey()
                logger.info("save form : [{}]", formId)
                wfFormService.saveFormData(formDataDto)
            }
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 폼 새이름으로 저장하기
     *
     * @param formData
     * @return String 새로 생성된 form ID
     */
    fun saveAsForm(formData: RestTemplateFormDataDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        var formMap: Map<String, String>? = null
        when (wfFormService.checkFormData(formId = null, formName = formData.name)) {
            WfFormConstants.Status.STATUS_ERROR_DUPLICATE_FORM_NAME.code -> {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
            else -> {
                formData.status = WorkflowConstants.FormStatus.EDIT.value
                formData.createUserKey = currentSessionUser.getUserKey()
                formData.createDt = LocalDateTime.now()
                val newFormId = wfFormService.saveAsFormData(formData).id
                logger.info("save as form : success [{}]", newFormId)
                formMap = mutableMapOf()
                formMap["formId"] = newFormId
            }
        }
        return ZResponse(
            status = status.code,
            data = formMap
        )
    }

    /**
     * 컴포넌트 템플릿 조회.
     * @return ZReponse
     */
    fun getComponentTemplateData(): ZResponse {
        return wfComponentTemplateService.getComponentTemplateData()
    }

    /**
     * 컴포넌트 템플릿 저장.
     * @param wfComponentTemplateDto
     * @return ZResponse
     */
    fun saveComponentTemplate(@RequestBody wfComponentTemplateDto: WfComponentTemplateDto): ZResponse {
        return wfComponentTemplateService.saveComponentTemplate(wfComponentTemplateDto)
    }

    /**
     * 컴포넌트 템플릿 삭제.
     * @param templateId
     * @return ZResponse
     */
    fun deleteComponentTemplate(@PathVariable templateId: String): ZResponse {
        return wfComponentTemplateService.deleteComponentTemplate(templateId)
    }
}
