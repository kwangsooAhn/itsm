package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.ConstructorManager
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.token.entity.WfTokenEntity
import java.time.LocalDateTime
import java.time.ZoneId

class WfSubProcessTokenManager(
    constructorManager: ConstructorManager
) : WfTokenManager(constructorManager) {

    private val wfTokenRepository = constructorManager.getTokenRepository()
    private val wfElementRepository = constructorManager.getElementRepository()
    private val wfTokenMappingValue = constructorManager.getTokenMappingValue()
    private val wfTokenDataRepository = constructorManager.getTokenDataRepository()
    private val wfInstanceRepository = constructorManager.getInstanceRepository()

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {

        val token = WfTokenEntity(
            tokenId = "",
            tokenStatus = RestTemplateConstants.TokenStatus.RUNNING.value,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = wfInstanceRepository.findByInstanceId(wfTokenDto.instanceId)!!,
            element = wfElementRepository.findWfElementEntityByElementId(wfTokenDto.elementId)
        )
        token.assigneeId = wfTokenDto.assigneeId
        val saveToken = wfTokenRepository.save(token)
        wfTokenDto.tokenId = saveToken.tokenId
        wfTokenDto.elementId = saveToken.element.elementId
        wfTokenDto.elementType = saveToken.element.elementType
        saveToken.tokenData = wfTokenDataRepository.saveAll(super.setTokenData(wfTokenDto))

        // MappingId를 찾아 넘어갈 데이터를 선정하고 프로세스를 시작한다.
        val elementInfo = wfElementRepository.findWfElementEntityByElementId(wfTokenDto.elementId)
        val documentId =
            super.getAttributeValue(
                elementInfo.elementDataEntities,
                WfElementConstants.AttributeId.SUB_DOCUMENT_ID.value
            )
        val startTokenDto = wfTokenDto.copy()
        startTokenDto.documentId = documentId
        startTokenDto.parentTokenId = startTokenDto.tokenId
        val makeDocumentTokens =
            wfTokenMappingValue.makeRestTemplateTokenDto(saveToken, mutableListOf(documentId))
        makeDocumentTokens.forEach {
            it.assigneeId = wfTokenDto.assigneeId
            WfEngine(constructorManager).startWorkflow(it)
        }

        return wfTokenDto
    }

}
