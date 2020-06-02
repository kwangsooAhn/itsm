package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.ConstructorManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.token.constants.WfTokenConstants
import java.time.LocalDateTime
import java.time.ZoneId

class WfCommonEndEventTokenManager(
    constructorManager: ConstructorManager
) : WfTokenManager(constructorManager) {

    private val wfInstanceService = constructorManager.getInstanceService()
    private val wfTokenRepository = constructorManager.getTokenRepository()
    private val wfTokenDataRepository = constructorManager.getTokenDataRepository()
    private val wfTokenMappingValue = constructorManager.getTokenMappingValue()

    override fun createNextToken(wfTokenDto: WfTokenDto): WfTokenDto {
        wfTokenDto.isAutoComplete = false //반복문을 종료한다.

        // TODO: 호출받은 token이 종료되었을 경우.. 호출한 프로세스를 다음단계로 진행한다. (subprocess만 조건으로 줘야하나???)
        if (!wfTokenDto.parentTokenId.isNullOrEmpty()) {
            if (wfTokenDto.elementType == WfElementConstants.ElementType.SUB_PROCESS.value) {
                val pTokenId = wfTokenDto.parentTokenId!!
                var token = wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId).get()
                token.tokenData = super.setTokenData(wfTokenDto)
                val mainProcessToken = wfTokenRepository.findTokenEntityByTokenId(pTokenId).get()
                mainProcessToken.tokenStatus = WfTokenConstants.Status.FINISH.code
                mainProcessToken.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
                wfTokenDto.data = wfTokenMappingValue.makeSubProcessTokenDataDto(
                    token,
                    mainProcessToken
                )
                wfTokenDto.tokenId = mainProcessToken.tokenId

                token = wfTokenRepository.save(mainProcessToken)
                token.tokenData = wfTokenDataRepository.saveAll(super.setTokenData(wfTokenDto))
                wfTokenDto.isAutoComplete = true
            }
        }

        return wfTokenDto
    }

    override fun completeToken(wfTokenDto: WfTokenDto): WfTokenDto {
        super.completeToken(wfTokenDto)
        wfInstanceService.completeInstance(wfTokenDto.instanceId)

        return wfTokenDto
    }
}
