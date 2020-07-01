package co.brainz.itsm.customCode.mapper

import co.brainz.itsm.customCode.dto.CustomCodeDto
import co.brainz.itsm.customCode.entity.CustomCodeEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface CustomCodeMapper {
    @Mappings(
        Mapping(source = "createUser.userName", target = "createUserName"),
        Mapping(source = "updateUser.userName", target = "updateUserName"),
        Mapping(target = "targetTableName", ignore = true),
        Mapping(target = "searchColumnName", ignore = true),
        Mapping(target = "valueColumnName", ignore = true),
        Mapping(target = "enabled", ignore = true)
    )
    fun toCustomCodeDto(customCodeEntity: CustomCodeEntity): CustomCodeDto

    @Mappings(
        Mapping(target = "createDt", ignore = true),
        Mapping(target = "updateDt", ignore = true),
        Mapping(target = "createUser", ignore = true),
        Mapping(target = "updateUser", ignore = true)
    )
    fun toCustomCodeEntity(customCodeDto: CustomCodeDto): CustomCodeEntity
}
