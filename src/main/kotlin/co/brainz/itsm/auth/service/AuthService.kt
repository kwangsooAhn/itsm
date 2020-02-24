package co.brainz.itsm.auth.service

import co.brainz.itsm.auth.dto.AuthDto
import co.brainz.itsm.auth.dto.AuthDetailDto
import co.brainz.itsm.auth.dto.AuthMenuDto
import co.brainz.itsm.auth.dto.AuthUrlDto
import co.brainz.framework.auth.dto.AliceAuthSimpleDto
import org.springframework.stereotype.Service
import co.brainz.itsm.auth.repository.AuthRepository
import co.brainz.framework.auth.repository.AliceMenuRepository
import co.brainz.framework.auth.repository.AliceMenuAuthMapRepository
import co.brainz.framework.auth.repository.AliceUrlRepository
import co.brainz.framework.auth.repository.AliceUrlAuthMapRepository
import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceMenuEntity
import co.brainz.framework.auth.entity.AliceMenuAuthMapEntity
import co.brainz.framework.auth.entity.AliceMenuAuthMapPk
import co.brainz.framework.auth.entity.AliceUrlEntity
import co.brainz.framework.auth.entity.AliceUrlAuthMapEntity
import co.brainz.framework.auth.entity.AliceUrlAuthMapPk
import co.brainz.framework.auth.entity.AliceUrlEntityPk

@Service
class AuthService(
        private val authRepository: AuthRepository,
        private val menuRepository: AliceMenuRepository,
        private val menuAuthMapRepository: AliceMenuAuthMapRepository,
        private val urlRepository: AliceUrlRepository,
        private val urlAuthMapRepository: AliceUrlAuthMapRepository
) {

    /**
     * 전체 권한 목록 조회
     */
    fun selectAuthList(): MutableList<AliceAuthEntity> {
        return authRepository.findByOrderByAuthNameAsc()
    }

    /**
     * 전체 메뉴 목록 조회
     */
    fun selectMenuList(): MutableList<AliceMenuEntity> {
        return menuRepository.findByOrderByMenuIdAsc()
    }
    
    /**
     * 전체 url 목록 조회
     */
    fun selectUrlList(): MutableList<AliceUrlEntity> {
        return urlRepository.findByOrderByUrlAsc()
    }
    
    /**
     * 권한 정보 삭제
     */
    fun deleteAuth(authId: String): String {
        val auth = authRepository.findByAuthId(authId)
        // 매핑 정보 확인
        val menuAuthMapCount = menuAuthMapRepository.findByAuth(auth).count()
        val urlAuthMapCount = urlAuthMapRepository.findByAuth(auth).count()
        
        // 매핑 정보 삭제
        if (menuAuthMapCount > 0 || urlAuthMapCount > 0 ) {
            auth.menuAuthMapEntities.forEach { menuAuthMap ->
                menuAuthMapRepository.deleteById(AliceMenuAuthMapPk(menuAuthMap.menu.menuId, auth.authId))
            }
//            auth.urlAuthMapEntities.forEach { urlAuthMap ->
//                urlAuthMapRepository.deleteById(AliceUrlAuthMapPk(AliceUrlEntityPk(urlAuthMap.url.method, urlAuthMap.url.url), auth.authId))
//            }
        }
        
        authRepository.deleteById(authId)
        return "true"
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

        menuRepository.findByMenuIdIn(authInfo.arrMenuId!!).forEach {menu ->
            menuAuthMapRepository.save(AliceMenuAuthMapEntity(menu, auth))
        }
        urlRepository.findByUrlIn(authInfo.arrUrl!!).forEach {url ->
            urlAuthMapRepository.save(AliceUrlAuthMapEntity(AliceUrlEntity(url.method, url.url), auth))
        }

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

        // 메뉴 매핑 정보 수정
        authRepository.findByAuthId(auth.authId).menuAuthMapEntities.forEach { menuAuthMap ->
            menuAuthMapRepository.deleteById(AliceMenuAuthMapPk(menuAuthMap.menu.menuId, auth.authId))
        }
        menuRepository.findByMenuIdIn(authInfo.arrMenuId!!).forEach { menu ->
            menuAuthMapRepository.save(AliceMenuAuthMapEntity(menu, auth))
        }
        // 메뉴 URL 정보 수정
//        authRepository.findByAuthId(auth.authId).urlAuthMapEntities.forEach { urlAuthMap ->
//            urlAuthMapRepository.deleteById(AliceUrlAuthMapPk(AliceUrlEntityPk(urlAuthMap.url.method, urlAuthMap.url.url), auth.authId))
//        }
//        urlRepository.findByUrlIn(authInfo.arrUrl!!).forEach {url ->
//            urlAuthMapRepository.save(AliceUrlAuthMapEntity(AliceUrlEntity(url.method, url.url), auth))
//        }

        return result.authId
    }

    /**
     * 권한 상세 정보 조회
     */
    fun selectDetailAuths(authId: String): List<AuthDto> {
        val dto = mutableListOf<AuthDto>()
        val authInfo = authRepository.findByAuthId(authId)
        val menuList = mutableListOf<AuthMenuDto>()
        val urlList = mutableListOf<AuthUrlDto>()

        authInfo.menuAuthMapEntities.forEach { authMenuMap ->
            menuList.add(AuthMenuDto(authMenuMap.auth.authId, authMenuMap.menu.menuId))
        }
        authInfo.urlAuthMapEntities.forEach { authUrlMap ->
            urlList.add(AuthUrlDto(authUrlMap.url.url, authUrlMap.url.method, authUrlMap.auth.authId))
        }

        dto.add(
                AuthDto(
                        authInfo.authId,
                        authInfo.authName,
                        authInfo.authDesc,
                        authInfo.createUser?.userName,
                        authInfo.createDt,
                        authInfo.updateUser?.userName,
                        authInfo.updateDt,
                        null,
                        menuList,
                        null,
                        urlList
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
