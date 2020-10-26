/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.service

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.repository.AliceAuthRepository
import co.brainz.framework.auth.repository.AliceMenuRepository
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.itsm.user.dto.UserListDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
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

    @Value("\${file.image.uri}")
    private val resourcesUriPath: String? = null

    @Throws(EmptyResultDataAccessException::class)
    fun loadUserByUsername(userId: String): AliceUserEntity {
        return aliceUserRepository.findByUserId(userId)
    }

    @Transactional
    fun getAuthInfo(aliceUserAuthDto: AliceUserAuthDto): AliceUserAuthDto {
        val authorities = mutableSetOf<SimpleGrantedAuthority>()
        val rolePrefix = "ROLE_"

        aliceUserAuthDto.userKey.let { userKey ->
            aliceUserRoleMapRepository.findUserRoleByUserKey(userKey).forEach { aliceRoleEntity ->
                authorities.add(SimpleGrantedAuthority(rolePrefix + aliceRoleEntity.roleId))
                aliceRoleEntity.roleAuthMapEntities.forEach { roleAuthMap ->
                    authorities.add(SimpleGrantedAuthority(roleAuthMap.auth.authId))
                }
            }
        }
        aliceUserAuthDto.grantedAuthorises = authorities
        aliceUserAuthDto.menus =
            aliceUserAuthDto.userKey.let { aliceMenuRepository.findByUserKey(aliceUserAuthDto.userKey) }
        aliceUserAuthDto.urls =
            aliceUserAuthDto.userKey.let { aliceAuthRepository.findByUserKey(aliceUserAuthDto.userKey) }
        return aliceUserAuthDto
    }

    @Throws(EmptyResultDataAccessException::class)
    fun loadUserByOauthKeyAndPlatform(oauthKey: String, platform: String): AliceUserEntity {
        return aliceUserRepository.findByOauthKeyAndPlatform(oauthKey, platform)
    }

    /**
     * 아바타 타입에 따른 경로를 반환한다. 이때 uploaded가 false를 기본값으로 본다.
     */
    fun makeAvatarPath(user: Any): String {
        var avatarType = ""
        var uploaded = false
        var avatarValue = ""
        var uploadedLocation = ""
        when (user) {
            is AliceUserAuthDto -> {
                avatarType = user.avatarType.toString()
                avatarValue = user.avatarValue.toString()
                uploaded = user.uploaded
                uploadedLocation = user.uploadedLocation
            }
            is UserListDto -> {
                avatarType = user.avatarType
                avatarValue = user.avatarValue
                uploaded = user.uploaded
                uploadedLocation = user.uploadedLocation
            }
            is AliceUserEntity -> {
                avatarType = user.avatarType
                avatarValue = user.avatarValue
                uploaded = user.uploaded
                uploadedLocation = user.uploadedLocation
            }
        }

        return when (avatarType) {
            AliceUserConstants.AvatarType.FILE.code ->
                if (uploaded) {
                    resourcesUriPath + "/" + AliceUserConstants.AVATAR_IMAGE_DIR + "/" + avatarValue
                } else {
                    AliceUserConstants.AVATAR_BASIC_FILE_PATH + AliceUserConstants.AVATAR_BASIC_FILE_NAME
                }
            AliceUserConstants.AvatarType.URL.code -> uploadedLocation
            else -> uploadedLocation
        }
    }
}
