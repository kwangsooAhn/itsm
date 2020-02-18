package co.brainz.framework.auth.mapper

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.entity.AliceUserEntity
import org.mapstruct.Mapper

@Mapper
public interface AliceUserAuthMapper {
    fun toAliceUserAuthDto(aliceUserEntity: AliceUserEntity): AliceUserAuthDto
}