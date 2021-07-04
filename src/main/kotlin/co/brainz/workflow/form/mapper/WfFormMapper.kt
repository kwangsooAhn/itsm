/**
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.form.mapper

import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.provider.dto.RestTemplateFormDataDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named

@Mapper
interface WfFormMapper {

    private val objMapper: ObjectMapper
        get() = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

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
        Mapping(source = "formDisplayOption", target = "display", qualifiedByName = ["stringToLinkedHashMap"]),
        Mapping(source = "formCategory", target = "category"),
        Mapping(source = "createUser.userKey", target = "createUserKey"),
        Mapping(source = "createUser.userName", target = "createUserName"),
        Mapping(source = "updateUser.userKey", target = "updateUserKey"),
        Mapping(source = "updateUser.userName", target = "updateUserName"),
        Mapping(target = "editable", ignore = true)
    )
    fun toFormViewDto(wfFormEntity: WfFormEntity): RestTemplateFormDto

    @Mappings(
        Mapping(source = "id", target = "id"),
        Mapping(source = "name", target = "name"),
        Mapping(source = "desc", target = "desc"),
        Mapping(source = "status", target = "status"),
        Mapping(source = "display", target = "display"),
        Mapping(source = "category", target = "category"),
        Mapping(source = "updateDt", target = "updateDt"),
        Mapping(source = "updateUserKey", target = "updateUserKey"),
        Mapping(target = "createUserName", ignore = true),
        Mapping(target = "updateUserName", ignore = true)
    )
    fun toRestTemplateFormDto(restTemplateFormDataDto: RestTemplateFormDataDto): RestTemplateFormDto

    @Named("stringToLinkedHashMap")
    fun stringToLinkedHashMap(str: String): LinkedHashMap<String, Any>? {
        val convertedValue: LinkedHashMap<String, Any>
        val linkedMapType = TypeFactory.defaultInstance()
            .constructMapType(LinkedHashMap::class.java, String::class.java, Any::class.java)

        convertedValue = objMapper.convertValue(str, linkedMapType)
        return convertedValue
    }
}
