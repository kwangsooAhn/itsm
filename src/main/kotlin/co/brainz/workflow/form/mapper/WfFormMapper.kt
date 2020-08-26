package co.brainz.workflow.form.mapper

import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.provider.dto.RestTemplateFormComponentListDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface WfFormMapper {
    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "name", ignore = true),
        Mapping(target = "status", ignore = true),
        Mapping(target = "desc", ignore = true),
        Mapping(target = "editable", ignore = true),
        Mapping(target = "createUserKey", ignore = true),
        Mapping(target = "createUserName", ignore = true),
        Mapping(target = "updateUserKey", ignore = true),
        Mapping(target = "updateUserName", ignore = true)
    )
    fun toFormDto(wfFormEntity: WfFormEntity): RestTemplateFormDto

    @Mappings(
        Mapping(source = "formId", target = "id"),
        Mapping(source = "formName", target = "name"),
        Mapping(source = "formDesc", target = "desc"),
        Mapping(source = "formStatus", target = "status"),
        Mapping(source = "createUser.userKey", target = "createUserKey"),
        Mapping(source = "createUser.userName", target = "createUserName"),
        Mapping(source = "updateUser.userKey", target = "updateUserKey"),
        Mapping(source = "updateUser.userName", target = "updateUserName"),
        Mapping(target = "editable", ignore = true)
    )
    fun toFormViewDto(wfFormEntity: WfFormEntity): RestTemplateFormDto

    @Mappings(
        Mapping(source = "formId", target = "id"),
        Mapping(source = "name", target = "name"),
        Mapping(source = "desc", target = "desc"),
        Mapping(source = "status", target = "status"),
        Mapping(source = "updateDt", target = "updateDt"),
        Mapping(source = "updateUserKey", target = "updateUserKey"),
        Mapping(target = "editable", ignore = true),
        Mapping(target = "createUserName", ignore = true),
        Mapping(target = "updateUserName", ignore = true),
        Mapping(target = "totalCount", ignore = true)
    )
    fun toRestTemplateFormDto(restTemplateFormComponentListDto: RestTemplateFormComponentListDto): RestTemplateFormDto
}
