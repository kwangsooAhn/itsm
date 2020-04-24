package co.brainz.workflow.engine.process.mapper

import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.process.dto.ProcessDto
import co.brainz.workflow.engine.process.dto.WfElementDto
import co.brainz.workflow.engine.process.dto.WfProcessDto
import co.brainz.workflow.engine.process.entity.WfProcessEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface WfProcessMapper {
    @Mappings(
        Mapping(source = "processId", target = "id"),
        Mapping(source = "processName", target = "name"),
        Mapping(source = "processDesc", target = "description"),
        Mapping(source = "processStatus", target = "status"),
        Mapping(source = "createDt", target = "createDt"),
        Mapping(source = "createUserKey", target = "createUserKey"),
        Mapping(source = "updateDt", target = "updateDt"),
        Mapping(source = "updateUserKey", target = "updateUserKey")
    )
    fun toWfProcessDto(processEntity: WfProcessEntity): WfProcessDto

    fun toProcessEntity(processDto: ProcessDto): WfProcessEntity

    @Mappings(
        Mapping(source = "elementId", target = "id"),
        Mapping(source = "elementType", target = "type")
    )
    fun toWfElementDto(elementEntity: WfElementEntity): WfElementDto
}
