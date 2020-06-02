package co.brainz.workflow.engine.manager.service

import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.token.entity.WfTokenEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WfTokenManagerService(
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfElementRepository: WfElementRepository
) {

    //필요한 함수를 여기에 모두 작성한다.

    fun makeTokenEntity(wfTokenDto: WfTokenDto): WfTokenEntity {
        return WfTokenEntity(
            tokenId = "",
            tokenStatus = RestTemplateConstants.TokenStatus.RUNNING.value,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = wfInstanceRepository.findByInstanceId(wfTokenDto.instanceId)!!,
            element = wfElementRepository.findWfElementEntityByElementId(wfTokenDto.elementId)
        )
    }
}
