package co.brainz.framework.auth.mapper

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import org.mapstruct.Mapper

@Mapper
interface AliceUserAuthMapper {
    fun toAliceUserAuthDto(aliceUserEntity: AliceUserEntity): AliceUserAuthDto
    fun toAliceUserEntity(aliceUserDto: AliceUserDto): AliceUserEntity
}