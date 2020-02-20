package co.brainz.itsm.auth.service


import co.brainz.itsm.auth.repository.AuthRepository
import co.brainz.itsm.auth.dto.AuthDetailDto
import co.brainz.itsm.auth.dto.AuthDto
import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceRoleAuthMapEntity
import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.repository.AliceAuthRepository
import co.brainz.framework.auth.repository.AliceRoleAuthMapRepository
import org.springframework.stereotype.Service
import co.brainz.framework.auth.dto.AliceAuthSimpleDto
import co.brainz.framework.auth.entity.AliceRoleAuthMapPk
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository

import co.brainz.itsm.role.dto.RoleDetailDto
import co.brainz.itsm.role.dto.RoleDto
import co.brainz.itsm.role.repository.RoleRepository

@Service
class AuthService(
        private val aliceAuthRepository: AliceAuthRepository,
        private val authRepository: AuthRepository //,
        // auth-menu repository
        //private val authMenuMapRepository: AliceMenuAuthMapRepository
) {

    /**
     * 전체 권한 정보 조회
     */
    fun selectAuthList(): MutableList<AliceAuthEntity> {
        return aliceAuthRepository.findByOrderByAuthIdAsc()
    }

    /**
     * 전체 메뉴 정보 조회
     */
    fun selectMenuList(): MutableList<AliceAuthEntity> {
        return aliceAuthRepository.findByOrderByAuthIdAsc()
    }
    
    /**
     * 권한 정보 삭제
     */
    fun deleteAuth(authId: String): String {
        val authInfo = authRepository.findByAuthId(authId)
//        val userAuthMapCount = userAuthMapRepository.findByAuth(authInfo).count()
        
        return "PlaseDeleteMapperUser"
        
//        return if (userAuthMapCount == 0) {
//            authInfo.authAuthMapEntities.forEach { authAuthMap ->
//                authAuthMapRepository.deleteById(AliceAuthAuthMapPk(authInfo.authId, authAuthMap.auth.authId))
//            }
//            authRepository.deleteById(authId)
//            "true"
//        } else {
//            "PlaseDeleteMapperUser"
//        }
    }

    /**
     * 권한 정보 등록
     */
    fun insertAuth(authInfo: AuthDto): String {
        val auth = AliceAuthEntity(
                authId = authInfo.authId.toString(),
                authName = authInfo.authName.toString(),
                authDesc = authInfo.authDesc.toString()
        )
        val result = authRepository.save(auth)

        // mapping 될 메뉴 정보
//        authRepository.findByAuthIdIn(authInfo.arrAuthId!!).forEach {auth ->
//            authAuthMapRepository.save(AliceAuthAuthMapEntity(auth, auth))
//        }

        return result.authId
    }

    /**
     * 권한 정보 수정
     */
    fun updateAuth(authInfo: AuthDto): String {
        val auth = AliceAuthEntity(
            authId = authInfo.authId.toString(),
            authName = authInfo.authName.toString(),
            authDesc = authInfo.authDesc.toString()
        )
        val result = authRepository.save(auth)
//
//        authRepository.findByAuthId(auth.authId).menuAuthMapEntities.forEach { authAuthMap ->
//            menuAuthMapEntities.deleteById(AliceAuthAuthMapPk(auth.authId, authAuthMap.auth.authId))
//        }
//        authRepository.findByAuthIdIn(authInfo.arrMenuId!!).forEach { auth ->
//            authAuthMapRepository.save(AliceAuthAuthMapEntity(auth, auth))
//        }

        return result.authId
    }

    /**
     * 권한 상세 정보 조회
     */
    fun selectDetailAuths(authId: String): List<AuthDto> {
        val dto = mutableListOf<AuthDto>()
        val authInfo = authRepository.findByAuthId(authId)
        val menuList = mutableListOf<AliceAuthSimpleDto>()

        authInfo.menuAuthMapEntities.forEach { //authAuthMap ->
//            menuList.add(AliceAuthSimpleDto(authAuthMap.auth.authId, authAuthMap.auth.authName, authAuthMap.auth.authDesc))
        }

        dto.add(
                AuthDto(
                        authInfo.authId,
                        authInfo.authName,
                        authInfo.authDesc,
                        authInfo.createUserKey,
                        authInfo.createDt,
                        authInfo.updateUserKey,
                        authInfo.updateDt,
                        null,
                        menuList
                )
        )
        return dto
    }

    /**
     * 모든 권한 목록을 조회한다.
     */
    fun getAuths(authEntities: Set<AliceAuthEntity>?): MutableList<AuthDetailDto> {
        val dto = mutableListOf<AuthDetailDto>()
        if (authEntities != null) {
            val getAuths = authRepository.findAll()
            var i = 0
            for (allAuth in getAuths) {
                for (dtoAuth in authEntities) {
                    if (dtoAuth.authId == allAuth.authId) {
                        i = 1
                        break
                    } else {
                        i = 0
                    }
                }
                if (i == 1) {
                    dto.add(AuthDetailDto(allAuth.authId, allAuth.authName, true))
                } else {
                    dto.add(AuthDetailDto(allAuth.authId, allAuth.authName, false))
                }
            }
        }
        return dto
    }

}
