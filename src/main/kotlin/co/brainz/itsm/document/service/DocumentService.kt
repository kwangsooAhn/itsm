/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.fileTransaction.constants.FileConstants
import co.brainz.framework.fileTransaction.provider.AliceFileProvider
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.AliceUtil
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.dto.DocumentDto
import co.brainz.itsm.document.dto.DocumentExportDto
import co.brainz.itsm.document.dto.DocumentImportDto
import co.brainz.itsm.document.dto.DocumentListReturnDto
import co.brainz.itsm.document.dto.DocumentSearchCondition
import co.brainz.itsm.document.dto.FieldDataDto
import co.brainz.itsm.document.dto.FieldDto
import co.brainz.itsm.document.dto.FieldOptionDto
import co.brainz.itsm.document.dto.FieldOrderDto
import co.brainz.itsm.document.dto.FieldReturnDto
import co.brainz.itsm.form.dto.FormSearchCondition
import co.brainz.itsm.form.service.FormService
import co.brainz.itsm.process.dto.ProcessSearchCondition
import co.brainz.itsm.process.service.ProcessAdminService
import co.brainz.itsm.process.service.ProcessService
import co.brainz.workflow.component.repository.WfComponentPropertyRepository
import co.brainz.workflow.document.repository.WfDocumentLinkRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.document.service.WfDocumentService
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.provider.constants.WorkflowConstants
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplaySaveDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplayViewDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import co.brainz.workflow.provider.dto.RestTemplateRequestDocumentDto
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.time.LocalDateTime
import kotlin.math.ceil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DocumentService(
    private val formService: FormService,
    private val processAdminService: ProcessAdminService,
    private val processService: ProcessService,
    private val wfDocumentService: WfDocumentService,
    private val aliceFileProvider: AliceFileProvider,
    private val currentSessionUser: CurrentSessionUser,
    private val wfDocumentLinkRepository: WfDocumentLinkRepository,
    private val wfDocumentRepository: WfDocumentRepository,
    private val wfComponentPropertyRepository: WfComponentPropertyRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 신청서 리스트 조회.
     *
     * @return List<RestTemplateDocumentListDto>
     */
    fun getDocumentList(documentSearchCondition: DocumentSearchCondition):
        DocumentListReturnDto {
        // 업무흐름을 관리하는 사용자라면 신청서 상태가 임시, 사용을 볼 수가 있다.
        val aliceUserDto = currentSessionUser.getUserDto()
        if (aliceUserDto!!.grantedAuthorises != null) {
            aliceUserDto.grantedAuthorises?.forEachIndexed { _, grantedAuthority ->
                if (grantedAuthority.authority == "workflow.manage") {
                    documentSearchCondition.viewType = "admin"
                }
            }
        }
        val documentQueryResult = wfDocumentRepository.findByDocuments(documentSearchCondition)
        val documentLinkQueryResult = wfDocumentLinkRepository.findByDocumentLink(documentSearchCondition)

        val totalResult = mutableListOf<DocumentDto>() // document + documentLink 합치기

        totalResult.addAll(documentQueryResult)
        if (documentSearchCondition.searchProcessName.isNullOrEmpty() && documentSearchCondition.searchFormName.isNullOrEmpty()) { // 폼, 프로세스 검색시 documnetLink 결과는 제외
            totalResult.addAll(documentLinkQueryResult)
        }
        totalResult.sortByDescending { it.createDt }

        var fromIndex = 0 // 문서함(documentSearch.html)에서 호출할 경우
        var toIndex = totalResult.size

        if (documentSearchCondition.pageNum > 0) { // 업무흐름(workflowSearch.html) 에서 호출할 경우
            fromIndex = ((documentSearchCondition.pageNum - 1) * documentSearchCondition.contentNumPerPage).toInt()
            toIndex = (fromIndex + documentSearchCondition.contentNumPerPage).toInt()

            if (totalResult.size < toIndex) {
                toIndex = totalResult.size
            }
        }

        val documentList = DocumentListReturnDto(
            data = if (totalResult.isNotEmpty()) totalResult.subList(fromIndex, toIndex) else totalResult,
            paging = AlicePagingData(
                totalCount = totalResult.size.toLong(),
                totalCountWithoutCondition = wfDocumentRepository.count() + wfDocumentLinkRepository.count(),
                currentPageNum = documentSearchCondition.pageNum,
                totalPageNum = ceil(totalResult.size / documentSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )

        for (document in documentList.data) {
            if (document.documentIcon.isNullOrEmpty()) document.documentIcon = DocumentConstants.DEFAULT_DOCUMENT_ICON
            document.documentIcon =
                aliceFileProvider.getDataUriSchema(
                    FileConstants.Path.ICON_DOCUMENT.path + File.separator + document.documentIcon
                )
        }
        return documentList
    }

    /**
     * 신청서 전체 조회.
     *
     * @return List<DocumentDto>
     */
    fun getDocumentAll(documentSearchCondition: DocumentSearchCondition):
            List<DocumentDto> {
        return wfDocumentService.allDocuments(documentSearchCondition)
    }

    /**
     * 신청서 조회.
     */
    fun getDocument(documentId: String): DocumentDto {
        return wfDocumentService.getDocument(documentId)
    }

    /**
     * 업무흐름 조회.
     */
    fun getDocumentAdmin(documentId: String): DocumentDto {
        return wfDocumentService.getDocument(documentId)
    }

    /**
     * 업무흐름링크 조회.
     */
    fun getDocumentLinkAdmin(documentId: String): DocumentDto {
        return wfDocumentService.getDocumentLink(documentId)
    }

    /**
     * 신청서 문서 데이터 조회.
     *
     * @return String
     */
    fun getDocumentData(documentId: String): RestTemplateRequestDocumentDto {
        return wfDocumentService.getInitDocument(documentId)
    }

    /**
     * 신청서 생성.
     *
     * @param documentDto
     */
    fun createDocument(documentDto: DocumentDto): ZResponse {
        documentDto.createUserKey = currentSessionUser.getUserKey()
        documentDto.createDt = LocalDateTime.now()
        return wfDocumentService.createDocument(documentDto)
    }

    /**
     * 신청서 링크 생성.
     *
     * @param documentDto
     */
    fun createDocumentLink(documentDto: DocumentDto): ZResponse {
        documentDto.createUserKey = currentSessionUser.getUserKey()
        documentDto.createDt = LocalDateTime.now()
        return wfDocumentService.createDocumentLink(documentDto)
    }

    /**
     * Update Document.
     *
     * @param documentDto
     * @return Boolean
     */
    fun updateDocument(
        documentDto: DocumentDto,
        params: LinkedHashMap<String, Any>
    ): ZResponse {
        documentDto.updateUserKey = currentSessionUser.getUserKey()
        documentDto.updateDt = LocalDateTime.now()
        return wfDocumentService.updateDocument(documentDto, params)
    }

    /**
     * Update DocumentLink.
     *
     * @param documentDto
     * @return String
     */
    fun updateDocumentLink(
        documentDto: DocumentDto
    ): ZResponse {
        documentDto.updateUserKey = currentSessionUser.getUserKey()
        documentDto.updateDt = LocalDateTime.now()
        return wfDocumentService.updateDocumentLink(documentDto)
    }

    /**
     * 신청서 삭제.
     *
     * @param documentId
     */
    fun deleteDocument(documentId: String): ZResponse {
        return wfDocumentService.deleteDocument(documentId)
    }

    /**
     * 신청서링크 삭제.
     *
     * @param documentId
     */
    fun deleteDocumentLink(documentId: String): ZResponse {
        return wfDocumentService.deleteDocumentLink(documentId)
    }

    /**
     * Get Form List.
     *
     * @return List<RestTemplateFormDto>
     */
    fun getFormList(): List<RestTemplateFormDto> {
        val formStatus = ArrayList<String>()
        formStatus.add(WorkflowConstants.FormStatus.PUBLISH.value)
        formStatus.add(WorkflowConstants.FormStatus.USE.value)
        return formService.findForms(
            FormSearchCondition(
                searchValue = "",
                status = formStatus.joinToString(","),
                pageNum = 0L,
                contentNumPerPage = 0L
            )
        ).data
    }

    /**
     * Get Process List.
     *
     * @return List<RestTemplateProcessViewDto>
     */
    fun getProcessList(): List<RestTemplateProcessViewDto> {
        val processStatus = ArrayList<String>()
        processStatus.add(WorkflowConstants.ProcessStatus.PUBLISH.value)
        processStatus.add(WorkflowConstants.ProcessStatus.USE.value)
        return processAdminService.getProcesses(
            ProcessSearchCondition(
                searchValue = "",
                status = processStatus.joinToString(","),
                pageNum = 0L,
                contentNumPerPage = 0L
            )
        ).data
    }

    /**
     * 신청서 양식 데이터 조회
     *
     * @param documentId
     * @return List<DocumentDto>
     */
    fun getDocumentDisplay(documentId: String): RestTemplateDocumentDisplayViewDto {
        return wfDocumentService.getDocumentDisplay(documentId)
    }

    /**
     * 신청서 양식 데이터 update
     *
     * @param documentDisplay
     */
    fun updateDocumentDisplay(documentDisplay: RestTemplateDocumentDisplaySaveDto): ZResponse {
        return wfDocumentService.updateDocumentDisplay(documentDisplay)
    }

    /**
     * 신청서 Export 데이터 조회.
     */
    fun getDocumentExportData(documentId: String): DocumentExportDto {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        // 프로세스 디자이너
        val processData = processService.getProcessData(documentEntity.process.processId)
        // 폼 디자이너
        val formData = formService.getFormData(documentEntity.form.formId)
        // 신청서 편집 양식
        val documentDisplayData: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        for (displayEntity in documentEntity.display) {
            val documentDisplayMap = LinkedHashMap<String, Any>()
            documentDisplayMap["formGroupId"] = displayEntity.formGroupId
            documentDisplayMap["elementId"] = displayEntity.elementId
            documentDisplayMap["display"] = displayEntity.display
            documentDisplayData.add(documentDisplayMap)
        }
        return DocumentExportDto(
            documentId = documentId,
            process = processData.process,
            elements = processData.elements,
            form = formData,
            displays = documentDisplayData
        )
    }

    /**
     * 신청서 Import.
     */
    fun importDocumentData(documentImportDto: DocumentImportDto): ZResponse {
        return wfDocumentService.importDocument(documentImportDto)
    }

    /**
     * 이력 조회 컴포넌트 데이터 조회
     */
    fun getDocumentComponentValue(documentNo: String, componentId: String): ZResponse {
        // wf_component_property 테이블에서 데이터 조회
        val componentProperties = wfComponentPropertyRepository.findByComponentId(componentId)
        val fieldOption = FieldOptionDto(documentNo = documentNo)
        componentProperties.forEach { property ->
            if (property.propertyType == WfFormConstants.PropertyType.ELEMENT.value) {
                val optionValue = mapper.readValue(property.propertyOptions, LinkedHashMap::class.java)
                fieldOption.table = optionValue["table"] as String
                fieldOption.keyField = optionValue["keyField"] as String
                fieldOption.fields.addAll(
                    mapper.convertValue(optionValue["fields"], object : TypeReference<List<FieldDto>>() {})
                )
                fieldOption.sort =
                    mapper.convertValue(optionValue["sort"], object : TypeReference<FieldOrderDto>() {})
            }
        }

        // fields
        val fieldDataList = mutableListOf<FieldDataDto>()
        fieldOption.fields.forEach {
            fieldDataList.add(
                FieldDataDto(
                    alias = it.alias,
                    width = it.width
                )
            )
        }

        // data (실패시 빈 데이터로 진행)
        val data = mutableListOf<Array<Any>>()
        try {
            val results = wfDocumentService.getSearchFieldValues(fieldOption)
            if (!results.isNullOrEmpty()) {
                data.add(results.toTypedArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error(AliceUtil().printStackTraceToString(e))
        }

        return ZResponse(
            data = FieldReturnDto(
                fields = fieldDataList,
                data = data
            )
        )
    }
}
