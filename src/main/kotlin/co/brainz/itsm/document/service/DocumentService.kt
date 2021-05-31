/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.fileTransaction.constants.FileConstants
import co.brainz.framework.fileTransaction.provider.AliceFileProvider
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.form.service.FormService
import co.brainz.itsm.process.service.ProcessAdminService
import co.brainz.workflow.document.service.WfDocumentService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplaySaveDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplayViewDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentListDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import co.brainz.workflow.provider.dto.RestTemplateRequestDocumentDto
import java.io.File
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class DocumentService(
    private val formService: FormService,
    private val processAdminService: ProcessAdminService,
    private val wfDocumentService: WfDocumentService,
    private val aliceFileProvider: AliceFileProvider
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 신청서 리스트 조회.
     *
     * @return List<RestTemplateDocumentListDto>
     */
    fun getDocumentList(restTemplateDocumentSearchListDto: RestTemplateDocumentSearchListDto):
            List<RestTemplateDocumentListDto> {
        // 업무흐름을 관리하는 사용자라면 신청서 상태가 임시, 사용을 볼 수가 있다.
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        if (aliceUserDto.grantedAuthorises != null) {
            aliceUserDto.grantedAuthorises.forEachIndexed { _, grantedAuthority ->
                if (grantedAuthority.authority == "document.read.admin") {
                    restTemplateDocumentSearchListDto.viewType = "admin"
                }
            }
        }
        val documentList = wfDocumentService.documents(restTemplateDocumentSearchListDto)

        for (document in documentList) {
            if (document.documentIcon.isNullOrEmpty()) document.documentIcon = DocumentConstants.DEFAULT_DOCUMENT_ICON
            document.documentIcon =
                aliceFileProvider.getDataUriSchema(FileConstants.Path.ICON_DOCUMENT.path + File.separator + document.documentIcon)
        }

        return documentList
    }

    /**
     * 신청서 전체 조회.
     *
     * @return List<DocumentDto>
     */
    fun getDocumentAll(restTemplateDocumentSearchListDto: RestTemplateDocumentSearchListDto):
            List<RestTemplateDocumentDto> {
        return wfDocumentService.allDocuments(restTemplateDocumentSearchListDto)
    }

    /**
     * 신청서 조회.
     */
    fun getDocument(documentId: String): RestTemplateDocumentDto {
        return wfDocumentService.getDocument(documentId)
    }

    /**
     * 업무흐름 조회.
     */
    fun getDocumentAdmin(documentId: String): RestTemplateDocumentDto {
        return wfDocumentService.getDocument(documentId)
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
     * @param restTemplateDocumentDto
     * @return String?
     */
    fun createDocument(restTemplateDocumentDto: RestTemplateDocumentDto): String? {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateDocumentDto.createUserKey = aliceUserDto.userKey
        restTemplateDocumentDto.createDt = LocalDateTime.now()
        val dataDto = wfDocumentService.createDocument(restTemplateDocumentDto)
        return dataDto.documentId
    }

    /**
     * Update Document.
     *
     * @param restTemplateDocumentDto
     * @return Boolean
     */
    fun updateDocument(
        restTemplateDocumentDto: RestTemplateDocumentDto,
        params: LinkedHashMap<String, Any>
    ): String? {
        val documentId = restTemplateDocumentDto.documentId
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateDocumentDto.updateUserKey = aliceUserDto.userKey
        restTemplateDocumentDto.updateDt = LocalDateTime.now()
        wfDocumentService.updateDocument(restTemplateDocumentDto, params)
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
     * Get Form List.
     *
     * @return List<RestTemplateFormDto>
     */
    fun getFormList(): List<RestTemplateFormDto> {
        val formParams = LinkedHashMap<String, Any>()
        val formStatus = ArrayList<String>()
        formStatus.add(RestTemplateConstants.FormStatus.PUBLISH.value)
        formStatus.add(RestTemplateConstants.FormStatus.USE.value)
        formParams["status"] = formStatus.joinToString(",")
        return formService.findForms(formParams)
    }

    /**
     * Get Process List.
     *
     * @return List<RestTemplateProcessViewDto>
     */
    fun getProcessList(): List<RestTemplateProcessViewDto> {
        val processParams = LinkedHashMap<String, Any>()
        val processStatus = ArrayList<String>()
        processStatus.add(RestTemplateConstants.ProcessStatus.PUBLISH.value)
        processStatus.add(RestTemplateConstants.ProcessStatus.USE.value)
        processParams["status"] = processStatus.joinToString(",")
        return processAdminService.getProcesses(processParams)
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
