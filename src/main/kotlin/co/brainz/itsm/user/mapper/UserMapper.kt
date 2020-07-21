/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.mapper

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.user.dto.UserDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface UserMapper {
    @Mappings(
        Mapping(target = "avatarPath", ignore = true)
    )
    fun toUserDto(aliceUserEntity: AliceUserEntity): UserDto
}
