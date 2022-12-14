/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.service

import co.brainz.framework.auth.entity.AliceDocumentRoleMapEntity
import co.brainz.framework.auth.repository.AliceDocumentRoleMapRepository
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.AliceUtil
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.dto.DocumentDto
import co.brainz.itsm.document.dto.DocumentEditDto
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
import co.brainz.itsm.process.service.ProcessService
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.role.service.RoleService
import co.brainz.workflow.component.repository.WfComponentPropertyRepository
import co.brainz.workflow.document.repository.WfDocumentLinkRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.document.service.WfDocumentService
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.process.service.WfProcessService
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
import java.time.LocalDateTime
import kotlin.math.ceil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DocumentService(
    private val formService: FormService,
    private val processService: ProcessService,
    private val roleService: RoleService,
    private val wfDocumentService: WfDocumentService,
    private val currentSessionUser: CurrentSessionUser,
    private val roleRepository: RoleRepository,
    private val aliceDocumentRoleMapRepository: AliceDocumentRoleMapRepository,
    private val wfDocumentLinkRepository: WfDocumentLinkRepository,
    private val wfDocumentRepository: WfDocumentRepository,
    private val wfComponentPropertyRepository: WfComponentPropertyRepository,
    private val wfProcessService: WfProcessService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * ????????? ????????? ??????.
     *
     * @return List<RestTemplateDocumentListDto>
     */
    fun getDocumentList(documentSearchCondition: DocumentSearchCondition):
        DocumentListReturnDto {
        // ??????????????? ???????????? ??????????????? ????????? ????????? ??????, ????????? ??? ?????? ??????.
        val aliceUserDto = currentSessionUser.getUserDto()
        if (aliceUserDto!!.grantedAuthorises != null) {
            aliceUserDto.grantedAuthorises?.forEachIndexed { _, grantedAuthority ->
                if (grantedAuthority.authority == "workflow.manage") {
                    documentSearchCondition.viewType = "admin"
                }
            }
        }

        val roleList = mutableListOf<String>()
        roleService.getUserRoleList(aliceUserDto.userKey).forEach { roleList.add(it.roleId) }
        val validDocumentIds =
            when (documentSearchCondition.searchDocumentType?.equals(DocumentConstants.DocumentType.DOCUMENT_SEARCH)) {
                true -> aliceDocumentRoleMapRepository.findDocumentIdsByRoles(roleList)
                else -> mutableListOf()
            }
        val documentQueryResult = wfDocumentRepository.findByDocuments(documentSearchCondition, validDocumentIds)
        val documentLinkQueryResult = wfDocumentLinkRepository.findByDocumentLink(documentSearchCondition, validDocumentIds)

        val totalResult = mutableListOf<DocumentDto>() // document + documentLink ?????????

        totalResult.addAll(documentQueryResult)
        // ???, ???????????? ????????? documnetLink ????????? ??????
        if (documentSearchCondition.searchProcessName.isNullOrEmpty() && documentSearchCondition.searchFormName.isNullOrEmpty()) {
            totalResult.addAll(documentLinkQueryResult)
        }
        totalResult.sortByDescending { it.createDt }

        var fromIndex = 0 // ?????????(documentSearch.html)?????? ????????? ??????
        var toIndex = totalResult.size

        if (documentSearchCondition.pageNum > 0) { // ????????????(workflowSearch.html) ?????? ????????? ??????
            fromIndex = ((documentSearchCondition.pageNum - 1) * documentSearchCondition.contentNumPerPage).toInt()
            toIndex = (fromIndex + documentSearchCondition.contentNumPerPage).toInt()

            if (totalResult.size < toIndex) {
                toIndex = totalResult.size
            }
        }

        return DocumentListReturnDto(
            data = if (totalResult.isNotEmpty()) totalResult.subList(fromIndex, toIndex) else totalResult,
            paging = AlicePagingData(
                totalCount = totalResult.size.toLong(),
                totalCountWithoutCondition = wfDocumentRepository.count() + wfDocumentLinkRepository.count(),
                currentPageNum = documentSearchCondition.pageNum,
                totalPageNum = ceil(totalResult.size / documentSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    /**
     * ????????? ?????? ??????.
     *
     * @return List<DocumentDto>
     */
    fun getDocumentAll(documentSearchCondition: DocumentSearchCondition):
        List<DocumentDto> {
        return wfDocumentService.allDocuments(documentSearchCondition)
    }

    /**
     * ????????? ??????.
     */
    fun getDocument(documentId: String): DocumentDto {
        return wfDocumentService.getDocument(documentId)
    }

    /**
     * ???????????? ??????.
     * .
     */
    fun getDocumentAdmin(documentId: String): DocumentDto {
        return wfDocumentService.getDocument(documentId)
    }

    /**
     * ?????????????????? ??????.
     */
    fun getDocumentLinkAdmin(documentId: String): DocumentDto {
        return wfDocumentService.getDocumentLink(documentId)
    }

    /**
     * ????????? ?????? ????????? ??????.
     *
     * @return String
     */
    fun getDocumentData(documentId: String): RestTemplateRequestDocumentDto {
        return wfDocumentService.getInitDocument(documentId)
    }

    /**
     * ????????? ??????.
     *
     * @param documentEditDto
     */
    fun createDocument(documentEditDto: DocumentEditDto): ZResponse {
        documentEditDto.createUserKey = currentSessionUser.getUserKey()
        documentEditDto.createDt = LocalDateTime.now()

        val result = wfDocumentService.createDocument(documentEditDto)
        if (result.status == ZResponseConstants.STATUS.SUCCESS.code) {
            documentEditDto.documentId = result.data.toString()
            updateDocumentRoleMapping(documentEditDto)
        }

        return result
    }

    /**
     * ????????? ?????? ??????.
     *
     * @param documentEditDto
     */
    fun createDocumentLink(documentEditDto: DocumentEditDto): ZResponse {
        documentEditDto.createUserKey = currentSessionUser.getUserKey()
        documentEditDto.createDt = LocalDateTime.now()

        val result = wfDocumentService.createDocumentLink(documentEditDto)
        if (result.status == ZResponseConstants.STATUS.SUCCESS.code) {
            documentEditDto.documentId = result.data.toString()
            documentEditDto.documentType = DocumentConstants.DocumentType.APPLICATION_FORM_LINK.value
            updateDocumentRoleMapping(documentEditDto)
        }
        return result
    }

    /**
     * Update Document.
     *
     * @param documentEditDto
     * @return Boolean
     */
    fun updateDocument(
        documentEditDto: DocumentEditDto,
        params: LinkedHashMap<String, Any>
    ): ZResponse {
        documentEditDto.updateUserKey = currentSessionUser.getUserKey()
        documentEditDto.updateDt = LocalDateTime.now()

        val result = wfDocumentService.updateDocument(documentEditDto, params)
        if (result.status == ZResponseConstants.STATUS.SUCCESS.code) {
            updateDocumentRoleMapping(documentEditDto)
        }

        return result
    }

    /**
     * Update DocumentLink.
     *
     * @param documentEditDto
     * @return String
     */
    fun updateDocumentLink(
        documentEditDto: DocumentEditDto
    ): ZResponse {
        documentEditDto.updateUserKey = currentSessionUser.getUserKey()
        documentEditDto.updateDt = LocalDateTime.now()

        val result = wfDocumentService.updateDocumentLink(documentEditDto)
        if (result.status == ZResponseConstants.STATUS.SUCCESS.code) {
            documentEditDto.documentType = DocumentConstants.DocumentType.APPLICATION_FORM_LINK.value
            updateDocumentRoleMapping(documentEditDto)
        }

        return result
    }

    /**
     * ????????? ??????.
     *
     * @param documentId
     */
    fun deleteDocument(documentId: String): ZResponse {
        aliceDocumentRoleMapRepository.deleteByDocumentId(documentId)
        return wfDocumentService.deleteDocument(documentId)
    }

    /**
     * ??????????????? ??????.
     *
     * @param documentId
     */
    fun deleteDocumentLink(documentId: String): ZResponse {
        aliceDocumentRoleMapRepository.deleteByDocumentId(documentId)
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
        return wfProcessService.getProcesses(
            ProcessSearchCondition(
                searchValue = "",
                status = processStatus.joinToString(","),
                pageNum = 0L,
                contentNumPerPage = 0L
            )
        ).data
    }

    /**
     * ????????? ?????? ????????? ??????
     *
     * @param documentId
     * @return List<DocumentDto>
     */
    fun getDocumentDisplay(documentId: String): RestTemplateDocumentDisplayViewDto {
        return wfDocumentService.getDocumentDisplay(documentId)
    }

    /**
     * ????????? ?????? ????????? update
     *
     * @param documentDisplay
     */
    fun updateDocumentDisplay(documentDisplay: RestTemplateDocumentDisplaySaveDto): ZResponse {
        return wfDocumentService.updateDocumentDisplay(documentDisplay)
    }

    /**
     * ????????? Export ????????? ??????.
     */
    fun getDocumentExportData(documentId: String): DocumentExportDto {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        // ???????????? ????????????
        val processData = processService.getProcessData(documentEntity.process.processId)
        // ??? ????????????
        val formData = formService.getFormData(documentEntity.form.formId)
        // ????????? ?????? ??????
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
     * ????????? Import.
     */
    fun importDocumentData(documentImportDto: DocumentImportDto): ZResponse {
        val result = wfDocumentService.importDocument(documentImportDto)
        val documentEditDto = documentImportDto.documentData

        if (result.status == ZResponseConstants.STATUS.SUCCESS.code) {
            documentEditDto.documentId = result.data.toString()
            updateDocumentRoleMapping(documentEditDto)
        }

        return result
    }

    /**
     * ?????? ?????? ???????????? ????????? ??????
     */
    fun getDocumentComponentValue(documentNo: String, componentId: String): ZResponse {
        // wf_component_property ??????????????? ????????? ??????
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

        // data (????????? ??? ???????????? ??????)
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

    /**
     * ????????? ?????? ????????? update
     *
     * @param documentEditDto
     */
    fun updateDocumentRoleMapping(documentEditDto: DocumentEditDto) {
        documentEditDto.documentRoles!!.forEach {
            aliceDocumentRoleMapRepository.save(
                AliceDocumentRoleMapEntity(
                    documentEditDto.documentId,
                    documentEditDto.documentType,
                    roleRepository.findByRoleId(it)
                )
            )
        }
    }
}
