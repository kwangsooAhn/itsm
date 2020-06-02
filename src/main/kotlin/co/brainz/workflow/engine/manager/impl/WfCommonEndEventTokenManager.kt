package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.ConstructorManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.service.WfTokenMappingValue
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfTokenDataEntity
import java.time.LocalDateTime
import java.time.ZoneId

class WfCommonEndEventTokenManager(
    constructorManager: ConstructorManager
) : WfTokenManager(constructorManager) {

    private val wfInstanceService = constructorManager.getInstanceService()
    private val wfTokenRepository = constructorManager.getTokenRepository()
    private val wfTokenDataRepository = constructorManager.getTokenDataRepository()
    private val wfTokenMappingValue = constructorManager.getTokenMappingValue()
    private val wfElementService = constructorManager.getElementService()

    override fun createNextToken(wfTokenDto: WfTokenDto): WfTokenDto {
        wfTokenDto.isAutoComplete = false //반복문을 종료한다.

        //마지막 wfTokenDto와 wfTokenEntity가 commonEnd이기 때문에 (아직 DB 저장전)
        //거기에는 데이터가 없다.
        //그 전 단계의 데이터를 가져와야 호출한 토큰과 비교해서 매핑키를 추출하여 해당 값만 변경한다.
        //dto 처리시... 기존 데이터는 유지하고 복사해서 사용할까???


        // TODO: 호출받은 token이 종료되었을 경우.. 호출한 프로세스를 다음단계로 진행한다. (subprocess만 조건으로 줘야하나???)
        if (!wfTokenDto.parentTokenId.isNullOrEmpty()) {

            //이미.. super.wfTokenEntity는 commonEnd이므로
            //그전단계의 데이터를 불러와야한다. 값은 wfTokenDto에 있나?



            //기존데이터에서 매핑된 데이터를 추출
            val pTokenId = super.wfTokenEntity.instance.pTokenId!!
            val mainProcessToken = wfTokenRepository.findTokenEntityByTokenId(pTokenId).get()
            mainProcessToken.tokenStatus = WfTokenConstants.Status.FINISH.code
            mainProcessToken.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            wfTokenDto.data = wfTokenMappingValue.makeSubProcessTokenDataDto(
                super.wfTokenEntity,
                mainProcessToken
            )
            wfTokenDto.tokenId = mainProcessToken.tokenId


            /*val pTokenId = super.wfTokenEntity.instance.pTokenId!!
            val mainProcessToken = wfTokenRepository.findTokenEntityByTokenId(pTokenId).get()
            mainProcessToken.tokenStatus = WfTokenConstants.Status.FINISH.code
            mainProcessToken.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))*/
            /*super.wfTokenEntity.tokenData = mainProcessToken.tokenData
            wfTokenDto.tokenId = mainProcessToken.tokenId
            wfTokenDto.data = wfTokenMappingValue.makeSubProcessTokenDataDto(
                super.wfTokenEntity,
                mainProcessToken
            )*/
            super.wfTokenEntity = wfTokenRepository.save(mainProcessToken)
            super.wfTokenEntity.tokenData = wfTokenDataRepository.saveAll(super.setTokenData(wfTokenDto))
            wfTokenDto.isAutoComplete = true
        }

        return wfTokenDto
    }

    override fun completeToken(wfTokenDto: WfTokenDto): WfTokenDto {
        super.completeToken(wfTokenDto)
        wfInstanceService.completeInstance(wfTokenDto.instanceId)

        return wfTokenDto
    }
}
