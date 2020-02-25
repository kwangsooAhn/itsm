package co.brainz.workflow.process.mapper

import co.brainz.workflow.process.dto.ProcessDto
import co.brainz.workflow.process.dto.WFElementDto
import co.brainz.workflow.process.dto.WFProcessDto
import co.brainz.workflow.process.entity.ElementMstEntity
import co.brainz.workflow.process.entity.ProcessMstEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface ProcessMstMapper {
    @Mappings(
        Mapping(source = "procId", target = "id"),
        Mapping(source = "procName", target = "name"),
        Mapping(source = "procDesc", target = "description"),
        Mapping(source = "procStatus", target = "status"),
        Mapping(source = "formMstEntity.formId", target = "formId"),
        Mapping(source = "formMstEntity.formName", target = "formName"),
        Mapping(source = "createDt", target = "createDt"),
        Mapping(source = "createUserKey", target = "createUserKey"),
        Mapping(source = "updateDt", target = "updateDt"),
        Mapping(source = "updateUserKey", target = "updateUserKey")
    )
    fun toWFProcessDto(processMstEntity: ProcessMstEntity): WFProcessDto

    @Mappings(
        Mapping(source = "elemId", target = "id"),
        Mapping(source = "elemType", target = "type"),
        Mapping(source = "displayInfo", target = "display")
    )
    fun toWFElementDto(elementMstEntity: ElementMstEntity): WFElementDto

    fun toProcessMstEntity(processDto: ProcessDto): ProcessMstEntity
}
