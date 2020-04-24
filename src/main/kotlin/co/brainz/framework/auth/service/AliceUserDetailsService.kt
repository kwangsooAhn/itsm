package co.brainz.framework.auth.service

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.repository.AliceAuthRepository
import co.brainz.framework.auth.repository.AliceMenuRepository
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AliceUserDetailsService(
    private var aliceUserRepository: AliceUserRepository,
    private var aliceAuthRepository: AliceAuthRepository,
    private var aliceMenuRepository: AliceMenuRepository,
    private var aliceUserRoleMapRepository: AliceUserRoleMapRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Throws(EmptyResultDataAccessException::class)
    fun loadUserByUsername(userId: String): AliceUserEntity {
        return aliceUserRepository.findByUserId(userId)
    }

    @Transactional
    fun getAuthInfo(aliceUserAuthDto: AliceUserAuthDto): AliceUserAuthDto {
        val authorities = mutableSetOf<SimpleGrantedAuthority>()
        val rolePrefix = "ROLE_"

        aliceUserAuthDto.userKey?.let { userKey ->
            aliceUserRoleMapRepository.findByUserKey(userKey).forEach { aliceRoleEntity ->
                authorities.add(SimpleGrantedAuthority(rolePrefix + aliceRoleEntity.roleId))
                aliceRoleEntity.roleAuthMapEntities.forEach { roleAuthMap ->
                    authorities.add(SimpleGrantedAuthority(roleAuthMap.auth.authId))
                }
            }
        }
        aliceUserAuthDto.grantedAuthorises = authorities
        aliceUserAuthDto.menus = aliceUserAuthDto.userKey?.let { aliceMenuRepository.findByUserKey(aliceUserAuthDto.userKey) }
        aliceUserAuthDto.urls = aliceUserAuthDto.userKey?.let { aliceAuthRepository.findByUserKey(aliceUserAuthDto.userKey) }
        return aliceUserAuthDto
    }

    @Throws(EmptyResultDataAccessException::class)
    fun loadUserByOauthKeyAndPlatform(oauthKey: String, platform: String): AliceUserEntity {
        return aliceUserRepository.findByOauthKeyAndPlatform(oauthKey, platform)
    }
}
