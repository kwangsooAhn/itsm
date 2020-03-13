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
            Mapping(source = "updateUser.userName", target = "updateUserName")
    )
    fun toCustomCodeDto(customCodeEntity: CustomCodeEntity): CustomCodeDto

    @Mappings(
            Mapping(target = "createDt", ignore = true),
            Mapping(target = "updateDt", ignore = true)
    )
    fun toCustomCodeEntity(customCodeDto: CustomCodeDto): CustomCodeEntity
}