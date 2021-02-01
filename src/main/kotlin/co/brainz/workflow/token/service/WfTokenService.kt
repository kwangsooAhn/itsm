package co.brainz.workflow.token.service

import co.brainz.itsm.cmdb.ci.constants.CIConstants
import co.brainz.itsm.cmdb.ci.service.CIService
import co.brainz.itsm.instance.constants.InstanceConstants
import co.brainz.itsm.instance.service.InstanceService
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.document.repository.WfDocumentDisplayRepository
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.service.WfActionService
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateTokenStakeholderViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenViewDto
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap

@Service
@Transactional
class WfTokenService(
    private val instanceService: InstanceService,
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfDocumentDisplayRepository: WfDocumentDisplayRepository,
    private val wfFormService: WfFormService,
    private val wfActionService: WfActionService,
    private val ciService: CIService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Search Tokens.
     *
     * @param parameters
     * @return List<LinkedHashMap<String, Any>>
     */
    fun getTokens(parameters: LinkedHashMap<String, Any>): List<RestTemplateTokenDto> {
        var assignee = ""
        var tokenStatus = ""
        if (parameters["assignee"] != null) {
            assignee = parameters["assignee"].toString()
        }
        if (parameters["tokenStatus"] != null) {
            tokenStatus = parameters["tokenStatus"].toString()
        }
        val tokenEntities = wfTokenRepository.findTokenMstEntityByAssigneeIdAndTokenStatus(
            assignee,
            tokenStatus
        )

        val returnValue: MutableList<RestTemplateTokenDto> = mutableListOf()

        for (tokenEntity in tokenEntities) {
            returnValue.add(
                RestTemplateTokenDto(
                    tokenId = tokenEntity.tokenId,
                    elementId = tokenEntity.element.elementId,
                    tokenStatus = tokenEntity.tokenStatus,
                    assigneeId = tokenEntity.assigneeId,
                    documentId = tokenEntity.instance.document.documentId,
                    documentName = tokenEntity.instance.document.documentName
                )
            )
        }

        return returnValue
    }

    /**
     * Token View.
     *
     * @param tokenId
     * @return LinkedHashMap<String, Any>
     */
    fun getToken(tokenId: String): RestTemplateTokenDto {
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId).get()
        val componentList: MutableList<RestTemplateTokenDataDto> = mutableListOf()
        tokenEntity.tokenDataEntities.forEach { data ->
            val tokenDataDto = RestTemplateTokenDataDto(
                componentId = data.component.componentId,
                value = data.value
            )
            componentList.add(tokenDataDto)
        }

        return RestTemplateTokenDto(
            tokenId = tokenEntity.tokenId,
            elementId = tokenEntity.element.elementId,
            assigneeId = tokenEntity.assigneeId,
            tokenStatus = tokenEntity.tokenStatus,
            isComplete = tokenEntity.tokenStatus == WfTokenConstants.Status.FINISH.code,
            instanceId = tokenEntity.instance.instanceId,
            documentId = tokenEntity.instance.document.documentId,
            documentName = tokenEntity.instance.document.documentName,
            data = componentList
        )
    }

    /**
     * Token + Instance View.
     *
     * @param tokenId
     * @return LinkedHashMap<String, Any>
     */
    fun getTokenData(tokenId: String): RestTemplateTokenViewDto {
        // FormComponent
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId)
        val formId = tokenEntity.get().instance.document.form.formId
        val formData = wfFormService.getFormComponentList(formId)

        val documentDisplayList =
            wfDocumentDisplayRepository.findByDocumentIdAndElementId(
                tokenEntity.get().instance.document.documentId,
                tokenEntity.get().element.elementId
            )

        val linkedMapType = TypeFactory.defaultInstance()
            .constructMapType(LinkedHashMap::class.java, String::class.java, Any::class.java)
        val listLinkedMapType = TypeFactory.defaultInstance().constructCollectionType(List::class.java, linkedMapType)

        for (componentEntity in formData.components) {
            // value
            val tokenDataEntities = wfTokenDataRepository.findWfTokenDataEntitiesByTokenTokenId(tokenId)
            for (tokenDataEntity in tokenDataEntities) {
                if (tokenDataEntity.component.componentId == componentEntity.componentId) {
                    var resultValue = tokenDataEntity.value
                    // CI 컴포넌트 - 세부 데이터 조회
                    if (resultValue.isNotEmpty() && componentEntity.type == WfComponentConstants.ComponentType.CI.code) {
                        val componentValue: Map<String, Any> =
                            mapper.readValue(resultValue, object : TypeReference<Map<String, Any>>() {})
                        val componentData: MutableMap<String, Any> =
                            mapper.convertValue(componentValue["componentData"], linkedMapType)
                        val valueCiList: List<MutableMap<String, Any>> =
                            mapper.convertValue(componentData["value"], listLinkedMapType)
                        valueCiList.forEach { data ->
                            val actionType = data["actionType"] as String
                            if (actionType == CIConstants.ActionType.DELETE.code || actionType == CIConstants.ActionType.READ.code) {
                                val ciData = ciService.getCI(data["ciId"] as String)
                                data["ciNo"] = ciData.ciNo as String
                                data["ciName"] = ciData.ciName as String
                                data["typeId"] = ciData.typeId as String
                                data["typeName"] = ciData.typeName as String
                                data["ciDesc"] = ciData.ciDesc ?: ""
                                data["ciIcon"] = ciData.ciIcon ?: ""
                                data["classId"] = ciData.classId as String
                            }
                        }
                        resultValue = mapper.writeValueAsString(valueCiList)
                    }
                    componentEntity.value = resultValue
                }
            }

            // displayType
            for (documentDisplay in documentDisplayList) {
                if (componentEntity.componentId == documentDisplay.componentId) {
                    componentEntity.dataAttribute["displayType"] = documentDisplay.display
                }
            }
        }

        val tokenData = LinkedMultiValueMap<String, String>()
        tokenData["tokenId"] = tokenEntity.get().tokenId
        tokenData["status"] = tokenEntity.get().tokenStatus
        return RestTemplateTokenViewDto(
            token = tokenData,
            instanceId = tokenEntity.get().instance.instanceId,
            form = formData,
            actions = wfActionService.actions(tokenEntity.get().element.elementId),
            stakeholders = this.getTokenStakeholders(tokenEntity.get())
        )
    }

    /**
     * 토큰정보[tokenEntity]를 받아서 문서관련자[RestTemplateTokenStakeholderViewDto]정보를 반환 한다.
     */
    private fun getTokenStakeholders(tokenEntity: WfTokenEntity): RestTemplateTokenStakeholderViewDto {
        var assigneeType = ""
        val assignees = mutableListOf<String>()
        tokenEntity.element.elementDataEntities.forEach { elementData ->
            if (elementData.attributeId == WfElementConstants.AttributeId.ASSIGNEE_TYPE.value) {
                assigneeType = elementData.attributeValue
            }
        }

        when (assigneeType) {
            WfTokenConstants.AssigneeType.ASSIGNEE.code -> assignees.add(tokenEntity.assigneeId.toString())
            WfTokenConstants.AssigneeType.USERS.code,
            WfTokenConstants.AssigneeType.GROUPS.code -> {
                tokenEntity.element.elementDataEntities.forEach { data ->
                    if (data.attributeId == WfElementConstants.AttributeId.ASSIGNEE.value) {
                        assignees.add(data.attributeValue)
                    }
                }
            }
        }
        return RestTemplateTokenStakeholderViewDto(
            type = assigneeType,
            assignee = assignees,
            revokeAssignee = this.getRevokeAssignee(tokenEntity.tokenId)
        )
    }

    /**
     * [tokenId]를 받아서 회수자 Id[String]를 반환 한다.
     */
    private fun getRevokeAssignee(tokenId: String): String {
        var assigneeId = ""
        var isBreak = true
        instanceService.getInstanceHistory(tokenId)?.sortedBy { it.tokenEndDt }?.reversed()
            ?.forEach { element ->
                if (element.tokenEndDt != null &&
                    element.elementType == InstanceConstants.ElementListForHistoryViewing.USER_TASK.value &&
                    isBreak
                ) {
                    assigneeId = element.assigneeId.toString()
                    isBreak = false
                }
            }
        return assigneeId
    }
}
