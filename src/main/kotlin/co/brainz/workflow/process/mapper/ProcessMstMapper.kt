package co.brainz.workflow.process.mapper

import co.brainz.workflow.process.dto.ProcessDto
import co.brainz.workflow.process.dto.WfJsonElementDto
import co.brainz.workflow.process.dto.WfJsonProcessDto
import co.brainz.workflow.process.entity.ElementMstEntity
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

    @Mappings(
        Mapping(source = "id", target = "processId"),
        Mapping(source = "name", target = "processName"),
        Mapping(source = "description", target = "processDesc"),
        Mapping(source = "status", target = "processStatus"),
        Mapping(source = "formId", target = "formMstEntity.formId"),
        Mapping(source = "formName", target = "formMstEntity.formName"),
        Mapping(source = "createDt", target = "createDt"),
        Mapping(source = "createUserKey", target = "createUserKey"),
        Mapping(source = "updateDt", target = "updateDt"),
        Mapping(source = "updateUserKey", target = "updateUserKey")
    )
    fun toProcessMstEntity(wfJsonProcessDto: WfJsonProcessDto): ProcessMstEntity
    fun toProcessMstEntity(processDto: ProcessDto): ProcessMstEntity

    @Mappings(
        Mapping(source = "elemId", target = "id"),
        Mapping(source = "elemType", target = "type")
    )
    fun toWfJsonElementDto(elementMstEntity: ElementMstEntity): WfJsonElementDto


    @Mappings(
        Mapping(source = "wfJsonProcessDto.id", target = "procId"),
        Mapping(source = "wfJsonElementDto.type", target = "elemType")
    )
    fun toElementMstEntity(wfJsonElementDto: WfJsonElementDto, wfJsonProcessDto: WfJsonProcessDto): ElementMstEntity


}
