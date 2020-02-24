package co.brainz.workflow.process.mapper

import co.brainz.workflow.process.dto.ProcessDto
import co.brainz.workflow.process.entity.ProcessMstEntity
import org.mapstruct.Mapper

@Mapper
interface ProcessMstMapper {
    fun toProcessMstEntity(processDto: ProcessDto): ProcessMstEntity
}