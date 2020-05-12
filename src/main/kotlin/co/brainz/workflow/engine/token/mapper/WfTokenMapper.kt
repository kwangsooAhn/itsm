package co.brainz.workflow.engine.token.mapper

import co.brainz.workflow.engine.token.entity.WfTokenDataEntity
import co.brainz.workflow.engine.token.entity.WfTokenEntity
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import org.mapstruct.Mapper

@Mapper
interface WfTokenMapper {
    fun toTokenDto(wfTokenEntity: WfTokenEntity): RestTemplateTokenDto
    fun toTokenDataDto(wfTokenDataEntity: WfTokenDataEntity): RestTemplateTokenDataDto
}
