/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.fileTransaction.constants.FileConstants
import co.brainz.framework.fileTransaction.provider.AliceFileProvider
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.dto.DocumentDto
import co.brainz.itsm.document.dto.DocumentListReturnDto
import co.brainz.itsm.document.dto.DocumentSearchCondition
import co.brainz.itsm.form.dto.FormSearchCondition
import co.brainz.itsm.form.service.FormService
import co.brainz.itsm.process.dto.ProcessSearchCondition
import co.brainz.itsm.process.service.ProcessAdminService
import co.brainz.workflow.document.repository.WfDocumentLinkRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.document.service.WfDocumentService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplaySaveDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplayViewDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import co.brainz.workflow.provider.dto.RestTemplateRequestDocumentDto
import java.io.File
import java.time.LocalDateTime
import kotlin.math.ceil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DocumentService(
    private val formService: FormService,
    private val processAdminService: ProcessAdminService,
    private val wfDocumentService: WfDocumentService,
    private val aliceFileProvider: AliceFileProvider,
    private val currentSessionUser: CurrentSessionUser,
    private val wfDocumentLinkRepository: WfDocumentLinkRepository,
    private val wfDocumentRepository: WfDocumentRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

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

        var totalResult = mutableListOf<DocumentDto>() // document + documentLink 합치기

        totalResult.addAll(documentQueryResult.results)
        if (documentSearchCondition.searchProcessName.equals("") && documentSearchCondition.searchFormName.equals("")) { // 폼, 프로세스 검색시 documnetLink 결과는 제외
            totalResult.addAll(documentLinkQueryResult.results)
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
                totalCount = documentQueryResult.total + documentLinkQueryResult.total,
                totalCountWithoutCondition = wfDocumentRepository.count() + wfDocumentLinkRepository.count(),
                currentPageNum = documentSearchCondition.pageNum,
                totalPageNum = ceil(totalResult.size / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
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
     * @return String?
     */
    fun createDocument(documentDto: DocumentDto): String? {
        documentDto.createUserKey = currentSessionUser.getUserKey()
        documentDto.createDt = LocalDateTime.now()

        val dataDto = wfDocumentService.createDocument(documentDto)
        return dataDto.documentId
    }

    /**
     * 신청서 링크 생성.
     *
     * @param documentDto
     * @return String?
     */
    fun createDocumentLink(documentDto: DocumentDto): String? {
        documentDto.createUserKey = currentSessionUser.getUserKey()
        documentDto.createDt = LocalDateTime.now()
        val dataDto = wfDocumentService.createDocumentLink(documentDto)

        return dataDto.documentId
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
    ): String? {
        val documentId = documentDto.documentId
        documentDto.updateUserKey = currentSessionUser.getUserKey()
        documentDto.updateDt = LocalDateTime.now()
        wfDocumentService.updateDocument(documentDto, params)
        return documentId
    }

    /**
     * Update Document.
     *
     * @param documentDto
     * @return Boolean
     */
    fun updateDocumentLink(
        documentDto: DocumentDto,
        params: LinkedHashMap<String, Any>
    ): String? {
        val documentId = documentDto.documentId
        documentDto.updateUserKey = currentSessionUser.getUserKey()
        documentDto.updateDt = LocalDateTime.now()
        wfDocumentService.updateDocumentLink(documentDto, params)
        return documentId
    }

    /**
     * 신청서 삭제.
     *
     * @param documentId
     * @return Boolean
     */
    fun deleteDocument(documentId: String): Boolean {
        return wfDocumentService.deleteDocument(documentId)
    }

    /**
     * 신청서링크 삭제.
     *
     * @param documentId
     * @return Boolean
     */
    fun deleteDocumentLink(documentId: String): Boolean {
        return wfDocumentService.deleteDocumentLink(documentId)
    }
    /**
     * Get Form List.
     *
     * @return List<RestTemplateFormDto>
     */
    fun getFormList(): List<RestTemplateFormDto> {
        val formStatus = ArrayList<String>()
        formStatus.add(RestTemplateConstants.FormStatus.PUBLISH.value)
        formStatus.add(RestTemplateConstants.FormStatus.USE.value)
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
        processStatus.add(RestTemplateConstants.ProcessStatus.PUBLISH.value)
        processStatus.add(RestTemplateConstants.ProcessStatus.USE.value)
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
     * @return Boolean
     */
    fun updateDocumentDisplay(documentDisplay: RestTemplateDocumentDisplaySaveDto): Boolean {
        return wfDocumentService.updateDocumentDisplay(documentDisplay)
    }
}
