/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.mapper

import co.brainz.framework.auth.dto.AliceIpVerificationDto
import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceIpVerificationEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.user.dto.UserDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface AliceUserAuthMapper {
    @Mappings(
        Mapping(target = "grantedAuthorises", ignore = true),
        Mapping(target = "menus", ignore = true),
        Mapping(target = "urls", ignore = true),
        Mapping(target = "avatarPath", ignore = true)
    )
    fun toAliceUserAuthDto(aliceUserEntity: AliceUserEntity): AliceUserAuthDto

    @Mappings(
        Mapping(target = "createUser", ignore = true),
        Mapping(target = "createDt", ignore = true),
        Mapping(target = "updateUser", ignore = true),
        Mapping(target = "updateDt", ignore = true),
        Mapping(target = "password", ignore = true),
        Mapping(target = "status", ignore = true),
        Mapping(target = "certificationCode", ignore = true),
        Mapping(target = "platform", ignore = true),
        Mapping(target = "userRoleMapEntities", ignore = true),
        Mapping(target = "avatarType", ignore = true),
        Mapping(target = "avatarValue", ignore = true),
        Mapping(target = "uploaded", ignore = true),
        Mapping(target = "uploadedLocation", ignore = true),
        Mapping(target = "userCustomEntities", ignore = true)
    )
    fun toAliceUserEntity(aliceUserDto: AliceUserDto): AliceUserEntity

    @Mappings(
        Mapping(target = "avatarPath", ignore = true),
        Mapping(target = "avatarId", ignore = true),
        Mapping(target = "avatarSize", ignore = true),
        Mapping(target = "totalCount", ignore = true)
    )
    fun toUserDto(aliceUserEntity: AliceUserEntity): UserDto

    @Mappings(
        Mapping(source = "createUser.userKey", target = "createUser"),
        Mapping(source = "updateUser.userKey", target = "updateUser")
    )
    fun toIpVerificationDto(ipVerificationEntity: AliceIpVerificationEntity): AliceIpVerificationDto
}
