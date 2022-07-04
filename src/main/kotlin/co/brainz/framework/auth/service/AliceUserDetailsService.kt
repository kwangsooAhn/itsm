/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.service

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.dto.AliceUserSimpleDto
import co.brainz.framework.auth.entity.AliceMenuEntity
import co.brainz.framework.auth.entity.AliceUrlEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.auth.repository.AliceMenuRepository
import co.brainz.framework.auth.repository.AliceRoleAuthMapRepository
import co.brainz.framework.auth.repository.AliceUrlRepository
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.encryption.AliceCryptoRsa
import co.brainz.framework.encryption.AliceEncryptionUtil
import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceUtil
import co.brainz.itsm.user.constants.UserConstants
import co.brainz.itsm.user.dto.UserListDataDto
import co.brainz.itsm.user.repository.UserRepository
import java.security.PrivateKey
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.RedirectStrategy
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Component
class AliceUserDetailsService(
    private var aliceUserRepository: AliceUserRepository,
    private var aliceUrlRepository: AliceUrlRepository,
    private var aliceMenuRepository: AliceMenuRepository,
    private var aliceUserRoleMapRepository: AliceUserRoleMapRepository,
    private var aliceRoleAuthMapRepository: AliceRoleAuthMapRepository,
    private var groupRepository: OrganizationRepository,
    private val userRepository: UserRepository,
    private val sessionRegistry: SessionRegistry,
    private val aliceCryptoRsa: AliceCryptoRsa,
    private val aliceEncryptionUtil: AliceEncryptionUtil
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val redirectStrategy: RedirectStrategy = DefaultRedirectStrategy()
    val aliceUserMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)

    @Value("\${file.image.uri}")
    private val resourcesUriPath: String? = null

    @Value("\${encryption.algorithm}")
    private val algorithm: String = ""

    @Throws(EmptyResultDataAccessException::class)
    fun loadUserByUsername(userId: String): AliceUserEntity {
        return aliceUserRepository.findByUserId(userId)
    }

    @Transactional
    fun getAuthInfo(aliceUserAuthDto: AliceUserAuthDto): AliceUserAuthDto {
        aliceUserAuthDto.grantedAuthorises = this.getAuthAndRole(aliceUserAuthDto)
        aliceUserAuthDto.menus = aliceUserAuthDto.userKey.let { userKey ->
            this.getMenus(aliceUserAuthDto)
        }
        aliceUserAuthDto.urls = aliceUserAuthDto.userKey.let { userKey ->
            this.getUrls(aliceUserAuthDto)
        }
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
            is UserListDataDto -> {
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
            UserConstants.AvatarType.FILE.code ->
                if (uploaded) {
                    resourcesUriPath + "/" + UserConstants.AVATAR_IMAGE_DIR + "/" + avatarValue
                } else {
                    UserConstants.AVATAR_BASIC_FILE_PATH + UserConstants.AVATAR_BASIC_FILE_NAME
                }
            UserConstants.AvatarType.URL.code -> uploadedLocation
            else -> uploadedLocation
        }
    }

    /**
     * 사용자 Key 로 조회
     */
    fun selectUserKey(userKey: String): AliceUserEntity {
        return aliceUserRepository.findByUserKey(userKey)
    }

    /**
     * Security Session 생성(갱신)
     */
    fun createNewAuthentication(userKey: String): Authentication {
        var aliceUser: AliceUserAuthDto = aliceUserMapper.toAliceUserAuthDto(this.selectUserKey(userKey))
        aliceUser = this.getAuthInfo(aliceUser)
        aliceUser.avatarPath = this.makeAvatarPath(aliceUser)
        val usernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(aliceUser.userId, aliceUser.password, aliceUser.grantedAuthorises)
        usernamePasswordAuthenticationToken.details = AliceUtil().setUserDetails(aliceUser)
        return usernamePasswordAuthenticationToken
    }

    private fun getAuthAndRole(aliceUserAuthDto: AliceUserAuthDto): MutableSet<SimpleGrantedAuthority> {
        val authorities = mutableSetOf<SimpleGrantedAuthority>()
        val rolePrefix = "ROLE_"

        aliceUserAuthDto.userKey.let { userKey ->
            val roleList = aliceUserRoleMapRepository.findUserRoleByUserKey(userKey)
            if (roleList.isNotEmpty()) {
                val roleIds = mutableSetOf<String>()
                for (role in roleList) {
                    roleIds.add(role.roleId)
                    authorities.add(SimpleGrantedAuthority(rolePrefix + role.roleId))
                }
                val authList = aliceRoleAuthMapRepository.findAuthByRoles(roleIds)
                for (auth in authList) {
                    authorities.add(SimpleGrantedAuthority(auth.authId))
                }
            }
        }

        // 사용자가 속한 Organization 이 보유한 역할 및 권한을 추가한다.
        if (!aliceUserAuthDto.department.isNullOrBlank()) {
            val roleList = mutableListOf<String>()
            groupRepository.findByIdOrNull(aliceUserAuthDto.department).let { organization ->
                organization?.organizationRoleMapEntities?.forEach {
                    roleList.add(it.role.roleId)
                }
            }
            if (roleList.isNotEmpty()) {
                val roleIds = mutableSetOf<String>()
                for (role in roleList) {
                    roleIds.add(role)
                    authorities.add(SimpleGrantedAuthority(rolePrefix + role))
                }
                val authList = aliceRoleAuthMapRepository.findAuthByRoles(roleIds)
                for (auth in authList) {
                    authorities.add(SimpleGrantedAuthority(auth.authId))
                }
            }
        }
        return authorities
    }

    private fun getMenus(aliceUserAuthDto: AliceUserAuthDto): MutableSet<AliceMenuEntity> {
        val aliceMenuData = aliceMenuRepository.findMenuByUserKey(aliceUserAuthDto.userKey)
        val menuEntities = mutableSetOf<AliceMenuEntity>()
        if (aliceMenuData.isNotEmpty()) {
            aliceMenuData.forEach { AliceMenuDto ->
                val aliceMenuEntity = AliceMenuEntity(
                    menuId = AliceMenuDto.menuId,
                    pMenuId = AliceMenuDto.pMenuId,
                    url = AliceMenuDto.url,
                    sort = AliceMenuDto.sort,
                    useYn = AliceMenuDto.useYn
                )
                menuEntities.add(aliceMenuEntity)
            }
        }
        // 사용자가 속한 Group이 접근 가능한 menu 데이터를 추가한다.
        if (!aliceUserAuthDto.department.isNullOrBlank()) {
            val aliceGroupMenuData = aliceMenuRepository.findMenuByGroupId(aliceUserAuthDto.department)
            aliceGroupMenuData.forEach { aliceGroupMenuDto ->
                val aliceMenuEntity = AliceMenuEntity(
                    menuId = aliceGroupMenuDto.menuId,
                    pMenuId = aliceGroupMenuDto.pMenuId,
                    url = aliceGroupMenuDto.url,
                    sort = aliceGroupMenuDto.sort,
                    useYn = aliceGroupMenuDto.useYn
                )
                menuEntities.add(aliceMenuEntity)
            }
        }
        return menuEntities
    }

    private fun getUrls(aliceUserAuthDto: AliceUserAuthDto): MutableSet<AliceUrlEntity> {
        val aliceUrlData = aliceUrlRepository.findUrlByUserKey(aliceUserAuthDto.userKey)
        val urlEntities = mutableSetOf<AliceUrlEntity>()
        if (aliceUrlData.isNotEmpty()) {
            aliceUrlData.forEach { AliceUrlDto ->
                val aliceUrlEntity = AliceUrlEntity(
                    url = AliceUrlDto.url,
                    method = AliceUrlDto.method,
                    urlDesc = AliceUrlDto.urlDesc!!,
                    requiredAuth = AliceUrlDto.isRequiredAuth!!
                )
                urlEntities.add(aliceUrlEntity)
            }
        }
        // 사용자가 속한 Group이 접근 가능한 url 데이터를 추가한다.
        if (!aliceUserAuthDto.department.isNullOrBlank()) {
            val aliceGroupUrlData = aliceUrlRepository.findUrlByGroupId(aliceUserAuthDto.department)
            aliceGroupUrlData.forEach { aliceGroupUrlDto ->
                val aliceUrlEntity = AliceUrlEntity(
                    url = aliceGroupUrlDto.url,
                    method = aliceGroupUrlDto.method,
                    urlDesc = aliceGroupUrlDto.urlDesc!!,
                    requiredAuth = aliceGroupUrlDto.isRequiredAuth!!
                )
                urlEntities.add(aliceUrlEntity)
            }
        }
        return urlEntities
    }

    /**
     * 사용자 확인, 같은 계정으로 로그인한 다른 세션이 있는지 체크
     */
    fun duplicateSessionCheck(userSimpleDto: AliceUserSimpleDto): ZResponse {
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val securityContextObject =
            attr.request.getSession(false)?.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY)

        val code: String
        if (securityContextObject != null) {
            code = ZResponseConstants.STATUS.SUCCESS_ALREADY_LOGIN.code
        } else {
            val privateKey = attr.request.session.getAttribute(AliceConstants.RsaKey.PRIVATE_KEY.value) as PrivateKey
            val userId = aliceCryptoRsa.decrypt(privateKey, userSimpleDto.userId)
            val password = aliceCryptoRsa.decrypt(privateKey, userSimpleDto.password)

            code = when (aliceUserRepository.existsByUserId(userId)) {
                false -> ZResponseConstants.STATUS.ERROR_INVALID_USER.code
                true -> {
                    val userEntity = userRepository.findByUserId(userId)
                    if (!userEntity.useYn) {
                        ZResponseConstants.STATUS.ERROR_DISABLED_USER.code
                    }
                    when (this.algorithm.toUpperCase()) {
                        AliceConstants.EncryptionAlgorithm.BCRYPT.value -> {
                            val bcryptPasswordEncoder = BCryptPasswordEncoder()
                            if (!bcryptPasswordEncoder.matches(password, userEntity.password)) {
                                ZResponseConstants.STATUS.ERROR_INVALID_USER.code
                            } else { // 중복 로그인 체크
                                when (sessionRegistry.allPrincipals.contains(userId)) {
                                    true -> ZResponseConstants.STATUS.ERROR_DUPLICATE_LOGIN.code
                                    false -> ZResponseConstants.STATUS.SUCCESS.code
                                }
                            }
                        }
                        AliceConstants.EncryptionAlgorithm.AES256.value, AliceConstants.EncryptionAlgorithm.SHA256.value -> {
                            val encryptPassword = aliceEncryptionUtil.encryptEncoder(password, this.algorithm)
                            if (encryptPassword != userEntity.password) {
                                ZResponseConstants.STATUS.ERROR_INVALID_USER.code
                            } else { // 중복 로그인 체크
                                when (sessionRegistry.allPrincipals.contains(userId)) {
                                    true -> ZResponseConstants.STATUS.ERROR_DUPLICATE_LOGIN.code
                                    false -> ZResponseConstants.STATUS.SUCCESS.code
                                }
                            }
                        }
                        else -> {
                            ZResponseConstants.STATUS.ERROR_INVALID_USER.code
                        }
                    }
                }
            }
        }
        return ZResponse(
            status = code
        )
    }
}
