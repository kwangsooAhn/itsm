package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.ConstructorManager
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto

class WfSubProcessTokenManager(
    constructorManager: ConstructorManager
) : WfTokenManager(constructorManager) {

    private val wfTokenRepository = constructorManager.getTokenRepository()
    private val wfInstanceService = constructorManager.getInstanceService()
    private val wfElementRepository = constructorManager.getElementRepository()

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        super.createToken(wfTokenDto)
        super.wfTokenEntity.assigneeId = wfTokenDto.assigneeId
        wfTokenRepository.save(super.wfTokenEntity)

        // Token Data를 저장한다. 서브프로세스단계에???
        val element = wfElementRepository.findWfElementEntityByElementId(wfTokenDto.elementId)
        val documentId =
            this.getAttributeValue(element.elementDataEntities, WfElementConstants.AttributeId.SUB_DOCUMENT_ID.value)


        // TODO: 신규 instance, token을 생성해야 한다.
        val startTokenDto = wfTokenDto.copy()
        startTokenDto.documentId = documentId

        val instance = wfInstanceService.createInstance(startTokenDto)
        startTokenDto.instanceId = instance.instanceId

        //createtoken
        val tokenManager = getTokenManager(startTokenDto.elementType)
        startTokenDto = tokenManager.createToken(startTokenDto)
        startTokenDto = tokenManager.completeToken(startTokenDto)

        // FirstToken Create
        val firstTokenDto = tokenManager.createNextToken(startTokenDto)



        //newTokenDto.
        WfEngine(constructorManager).progressWorkflow(firstTokenDto)
        // instnace
        // commonstart 까지만 생성하고.. 다음으로 진행... 최초처럼 신청서 작성 단계를 별도로 만들지 않는다.


        return wfTokenDto
    }

    /**
     * Get AttributeValue.
     *
     * @param elementDataEntities
     * @param attributeId
     * @return String (attributeValue)
     */
    private fun getAttributeValue(
        elementDataEntities: MutableList<WfElementDataEntity>,
        attributeId: String
    ): String {
        var attributeValue = ""
        elementDataEntities.forEach { data ->
            if (data.attributeId == attributeId) {
                attributeValue = data.attributeValue
            }
        }
        return attributeValue
    }

}
