package co.brainz.workflow.process.mapper

import co.brainz.workflow.element.entity.ElementMstEntity
import co.brainz.workflow.process.dto.ProcessDto
import co.brainz.workflow.process.dto.WfJsonElementDto
import co.brainz.workflow.process.dto.WfJsonProcessDto
import co.brainz.workflow.process.entity.ProcessMstEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings


@Mapper
interface ProcessMstMapper {
    @Mappings(
        Mapping(source = "processId", target = "id"),
        Mapping(source = "processName", target = "name"),
        Mapping(source = "processDesc", target = "description"),
        Mapping(source = "processStatus", target = "status"),
        Mapping(source = "formMstEntity.formId", target = "formId"),
        Mapping(source = "formMstEntity.formName", target = "formName"),
        Mapping(source = "createDt", target = "createDt"),
        Mapping(source = "createUserKey", target = "createUserKey"),
        Mapping(source = "updateDt", target = "updateDt"),
        Mapping(source = "updateUserKey", target = "updateUserKey")
    )
    fun toWfJsonProcessDto(processMstEntity: ProcessMstEntity): WfJsonProcessDto

    fun toProcessMstEntity(processDto: ProcessDto): ProcessMstEntity

    @Mappings(
        Mapping(source = "elementId", target = "id"),
        Mapping(source = "elementType", target = "type")
    )
    fun toWfJsonElementDto(elementMstEntity: ElementMstEntity): WfJsonElementDto



}
