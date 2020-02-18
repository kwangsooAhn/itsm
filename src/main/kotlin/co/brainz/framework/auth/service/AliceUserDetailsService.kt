package co.brainz.framework.auth.service

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.auth.repository.AliceAuthRepository
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import org.mapstruct.factory.Mappers
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class AliceUserDetailsService(
    private var aliceUserRepository: AliceUserRepository,
    private var aliceAuthRepository: AliceAuthRepository,
    private var aliceUserRoleMapRepository: AliceUserRoleMapRepository
) {
    private val aliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)

    @Throws(EmptyResultDataAccessException::class)
    fun loadUserByUsername(userId: String): AliceUserAuthDto {
        return aliceUserRepository.findByUserInfo(userId)
    }

    fun getAuthInfo(aliceUserAuthDto: AliceUserAuthDto): AliceUserAuthDto {
        val authorities = mutableSetOf<SimpleGrantedAuthority>()
        val authList = mutableListOf<AliceAuthEntity>()
        val rolePrefix = "ROLE_"
        aliceUserRoleMapRepository.findByUserKey(aliceUserAuthDto.userKey).forEach {aliceRoleEntity ->
            authorities.add(SimpleGrantedAuthority(rolePrefix + aliceRoleEntity.roleId))
            aliceRoleEntity.roleAuthMapEntities.forEach{roleAuthMap ->
                    authorities.add(SimpleGrantedAuthority(roleAuthMap.auth.authId))
                }
            }
        aliceUserAuthDto.grantedAuthories = authorities
        aliceUserAuthDto.menus = aliceMenuRepository.findByUserKey(aliceUserAuthDto.userKey)
        aliceUserAuthDto.urls = aliceUrlRepository.findByUserKey(aliceUserAuthDto.userKey)
        return aliceUserAuthDto
    }

    fun getAuthList(authIds: MutableSet<String>): MutableSet<AliceAuthEntity> {
        return aliceAuthRepository.findByAuthIdIn(authIds)
    }

    @Throws(EmptyResultDataAccessException::class)
    fun loadUserByOauthKeyAndPlatform(oauthKey: String, platform: String): AliceUserEntity {
        return aliceUserRepository.findByOauthKeyAndPlatform(oauthKey, platform)
    }

}
