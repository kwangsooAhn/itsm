package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.ConstructorManager
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto

class WfSubProcessTokenManager(
    constructorManager: ConstructorManager
) : WfTokenManager(constructorManager) {

    private val wfTokenRepository = constructorManager.getTokenRepository()
    private val wfElementRepository = constructorManager.getElementRepository()
    private val wfTokenMappingValue = constructorManager.getTokenMappingValue()
    private val wfTokenDataRepository = constructorManager.getTokenDataRepository()

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        super.createToken(wfTokenDto)
        super.wfTokenEntity.assigneeId = wfTokenDto.assigneeId
        super.wfTokenEntity = wfTokenRepository.save(super.wfTokenEntity)
        super.wfTokenEntity.tokenData = wfTokenDataRepository.saveAll(super.setTokenData(wfTokenDto))
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
            wfTokenMappingValue.makeRestTemplateTokenDto(super.wfTokenEntity, mutableListOf(documentId))
        makeDocumentTokens.forEach {
            it.instanceCreateUser = super.wfTokenEntity.instance.instanceCreateUser
            WfEngine(constructorManager).startWorkflow(it)
        }

        return wfTokenDto
    }

}
