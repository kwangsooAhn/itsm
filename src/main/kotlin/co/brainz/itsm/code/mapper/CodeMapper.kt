package co.brainz.itsm.code.mapper

import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.entity.CodeEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface CodeMapper {
    @Mappings(
        Mapping(source = "createUser.userName", target = "createUserName"),
        Mapping(source = "updateUser.userName", target = "updateUserName")
    )
    fun toCodeDto(codeEntity: CodeEntity): CodeDto

    @Mappings(
        Mapping(target = "createDt", ignore = true),
        Mapping(target = "updateDt", ignore = true),
        Mapping(target = "createUser", ignore = true),
        Mapping(target = "updateUser", ignore = true)
    )
    fun toCodeEntity(codeDto: CodeDto): CodeEntity
}
