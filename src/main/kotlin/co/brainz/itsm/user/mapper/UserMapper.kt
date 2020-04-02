package co.brainz.itsm.user.mapper

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.user.dto.UserDto
import org.mapstruct.Mapper

@Mapper
interface UserMapper {
    fun toUserDto(aliceUserEntity: AliceUserEntity): UserDto
}
