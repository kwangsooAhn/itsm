package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.engine.manager.ConstructorManager
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.token.entity.WfTokenEntity
import java.time.LocalDateTime
import java.time.ZoneId

class WfManualTaskTokenManager(
    constructorManager: ConstructorManager
) : WfTokenManager(constructorManager) {

    private val wfTokenRepository = constructorManager.getTokenRepository()
    private val wfInstanceRepository = constructorManager.getInstanceRepository()
    private val wfElementRepository = constructorManager.getElementRepository()

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        //super.createToken(wfTokenDto)

        val token = WfTokenEntity(
            tokenId = "",
            tokenStatus = RestTemplateConstants.TokenStatus.RUNNING.value,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = wfInstanceRepository.findByInstanceId(wfTokenDto.instanceId)!!,
            element = wfElementRepository.findWfElementEntityByElementId(wfTokenDto.elementId)
        )
        val saveToken = wfTokenRepository.save(token)
        wfTokenDto.tokenId = saveToken.tokenId
        wfTokenDto.elementId = saveToken.element.elementId
        wfTokenDto.elementType = saveToken.element.elementType

        token.assigneeId = wfTokenDto.assigneeId
        wfTokenRepository.save(token)
        return wfTokenDto
    }

}
