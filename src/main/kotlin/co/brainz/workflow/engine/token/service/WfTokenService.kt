package co.brainz.workflow.engine.token.service

import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.form.service.WfFormService
import co.brainz.workflow.engine.instance.dto.WfInstanceDto
import co.brainz.workflow.engine.instance.service.WfInstanceService
import co.brainz.workflow.engine.token.constants.WfTokenConstants
import co.brainz.workflow.engine.token.dto.WfTokenDataDto
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.dto.WfTokenViewDto
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WfTokenService(
        private val wfDocumentRepository: WfDocumentRepository,
        private val wfTokenRepository: WfTokenRepository,
        private val wfTokenDataRepository: WfTokenDataRepository,
        private val wfInstanceService: WfInstanceService,
        private val wfFormService: WfFormService,
        private val wfActionService: WfActionService,
        private val wfTokenElementService: WfTokenElementService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Search Tokens.
     *
     * @param parameters
     * @return List<LinkedHashMap<String, Any>>
     */
    fun getTokens(parameters: LinkedHashMap<String, Any>): List<LinkedHashMap<String, Any>> {
        var assignee = ""
        var assigneeType = ""
        var tokenStatus = ""
        if (parameters["assignee"] != null) {
            assignee = parameters["assignee"].toString()
        }
        if (parameters["assigneeType"] != null) {
            assigneeType = parameters["assigneeType"].toString()
        }
        if (parameters["tokenStatus"] != null) {
            tokenStatus = parameters["tokenStatus"].toString()
        }
        val tokenEntities = wfTokenRepository.findTokenMstEntityByAssigneeIdAndAssigneeTypeAndTokenStatus(
            assignee,
            assigneeType,
            tokenStatus
        )
        val returnValue: MutableList<LinkedHashMap<String, Any>> = mutableListOf()

        for (tokenEntity in tokenEntities) {
            val tokenDto = WfTokenDto(
                tokenId = tokenEntity.tokenId,
                elementId = tokenEntity.elementId,
                tokenStatus = tokenEntity.tokenStatus,
                assigneeId = tokenEntity.assigneeId,
                assigneeType = tokenEntity.assigneeType,
                documentId = tokenEntity.instance.document.documentId,
                documentName = tokenEntity.instance.document.documentName
            )

            val tokenMap = LinkedHashMap<String, Any>()
            tokenMap["token"] = tokenDto
            returnValue.add(tokenMap)
        }

        return returnValue
    }

    /**
     * Token View.
     *
     * @param tokenId
     * @return LinkedHashMap<String, Any>
     */
    fun getToken(tokenId: String): WfTokenDto {
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId)
        val tokenDataEntities = wfTokenDataRepository.findTokenDataEntityByTokenId(tokenId)
        val componentList: MutableList<WfTokenDataDto> = mutableListOf()
        for (tokenDataEntity in tokenDataEntities) {
            val tokenDataDto = WfTokenDataDto(
                componentId = tokenDataEntity.componentId,
                value = tokenDataEntity.value
            )
            componentList.add(tokenDataDto)
        }

        return WfTokenDto(
            tokenId = tokenEntity.get().tokenId,
            elementId = tokenEntity.get().elementId,
            assigneeType = tokenEntity.get().assigneeType,
            assigneeId = tokenEntity.get().assigneeId,
            tokenStatus = tokenEntity.get().tokenStatus,
            isComplete = tokenEntity.get().tokenStatus == WfTokenConstants.Status.FINISH.code,
            instanceId = tokenEntity.get().instance.instanceId,
            documentId = tokenEntity.get().instance.document.documentId,
            documentName = tokenEntity.get().instance.document.documentName,
            data = componentList
        )

        /*val returnValue = LinkedHashMap<String, Any>()
        returnValue["token"] = tokenDto*/

        //return tokenDto
    }

    /**
     * Token + Instance View.
     *
     * @param tokenId
     * @return LinkedHashMap<String, Any>
     */
    fun getTokenData(tokenId: String): LinkedHashMap<String, Any> {
        val tokenMstEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId)
        val componentEntities = tokenMstEntity.get().instance.document.form.components
        val tokenDataEntities = wfTokenDataRepository.findTokenDataEntityByTokenId(tokenId)

        val componentList: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        if (componentEntities != null) {
            for (componentEntity in componentEntities) {
                val attributes = wfFormService.makeAttributes(componentEntity)
                val values: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
                for (tokenDataEntity in tokenDataEntities) {
                    if (tokenDataEntity.componentId == componentEntity.componentId) {
                        val valueMap = LinkedHashMap<String, Any>()
                        valueMap["value"] = tokenDataEntity.value
                        values.add(valueMap)
                    }
                }

                val component = LinkedHashMap<String, Any>()
                component["componentId"] = componentEntity.componentId
                component["attributes"] = attributes
                component["values"] = values
                componentList.add(component)
            }
        }

        val componentsMap = LinkedHashMap<String, Any>()
        componentsMap["components"] = componentList

        val tokenViewDto = WfTokenViewDto(
            tokenId = tokenMstEntity.get().tokenId,
            components = componentList,
            actions = wfActionService.actions(tokenMstEntity.get().elementId)
        )

        val returnValue = LinkedHashMap<String, Any>()
        returnValue["token"] = tokenViewDto

        return returnValue
    }

    /**
     * Init Token.
     *
     * @param wfTokenDto
     */
    fun initToken(wfTokenDto: WfTokenDto) {
        val documentDto = wfTokenDto.documentId?.let { wfDocumentRepository.findDocumentEntityByDocumentId(it) }
        val processId = documentDto?.process?.processId
        val instanceDto = documentDto?.let { WfInstanceDto(instanceId = "", document = it) }
        val instance = instanceDto?.let { wfInstanceService.createInstance(it) }

        val wfTokenEntity = wfTokenElementService.initStart(wfTokenDto, processId, instance)
        wfTokenDto.tokenId = wfTokenEntity?.tokenId.toString()
        wfTokenDto.assigneeId = wfTokenEntity?.assigneeId
        wfTokenDto.assigneeType = wfTokenEntity?.assigneeType

        setTokenGate(wfTokenDto)
    }

    /**
     * Token Gate.
     *
     * @param wfTokenDto
     */
    fun setTokenGate(wfTokenDto: WfTokenDto) {
        val wfTokenEntity = wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId).get()
        val wfElementEntity = wfActionService.getElement(wfTokenEntity.elementId)
        logger.debug("Token Element Type : {}", wfElementEntity.elementType)
        when (wfElementEntity.elementType) {
            WfElementConstants.ElementType.USER_TASK.value -> wfTokenElementService.setUserTask(wfTokenEntity, wfElementEntity, wfTokenDto)
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> wfTokenElementService.setCommonEndEvent(wfTokenEntity, wfTokenDto)
            WfElementConstants.ElementType.COMMON_SUBPROCESS.value -> wfTokenElementService.setSubProcess(wfTokenEntity, wfTokenDto)
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> wfTokenElementService.setExclusiveGateway(wfTokenEntity, wfTokenDto)
        }
    }

    /**
     * Get Token Assignee.
     *
     * @param assigneeData
     * @return String
     */
    private fun getAssigneeForToken(assigneeData: String?): String {
        // 데이터가 만약 mappingExpresion('${xxxx}'와 같은) 이라면 문서에서 해당 코드로 매핑된 데이터를 찾아서 사용.
        // 데이터가 단순히 문자열이라면 그대로 사용.
        var assigneeForToken = ""

        val mappingExpr = WfTokenConstants.mappingExpression.toRegex()
        assigneeData?.let {
            assigneeForToken =
                if (mappingExpr.matches(assigneeData)) it.substring(2, assigneeData.length - 1) else assigneeData
        }
        return assigneeForToken
    }

}
