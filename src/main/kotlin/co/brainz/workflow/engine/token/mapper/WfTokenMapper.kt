package co.brainz.workflow.engine.token.mapper

import co.brainz.workflow.engine.token.dto.WfTokenDataDto
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.entity.WfTokenDataEntity
import co.brainz.workflow.engine.token.entity.WfTokenEntity
import org.mapstruct.Mapper

@Mapper
interface WfTokenMapper {
    fun toTokenDto(wfTokenEntity: WfTokenEntity): WfTokenDto
    fun toTokenDataDto(wfTokenDataEntity: WfTokenDataEntity): WfTokenDataDto
}
