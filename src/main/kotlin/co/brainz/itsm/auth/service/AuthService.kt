package co.brainz.itsm.auth.service

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceMenuAuthMapEntity
import co.brainz.framework.auth.entity.AliceMenuAuthMapPk
import co.brainz.framework.auth.entity.AliceMenuEntity
import co.brainz.framework.auth.entity.AliceUrlAuthMapEntity
import co.brainz.framework.auth.entity.AliceUrlAuthMapPk
import co.brainz.framework.auth.entity.AliceUrlEntity
import co.brainz.framework.auth.entity.AliceUrlEntityPk
import co.brainz.framework.auth.repository.AliceMenuAuthMapRepository
import co.brainz.framework.auth.repository.AliceMenuRepository
import co.brainz.framework.auth.repository.AliceRoleAuthMapRepository
import co.brainz.framework.auth.repository.AliceUrlAuthMapRepository
import co.brainz.framework.auth.repository.AliceUrlRepository
import co.brainz.itsm.auth.dto.AuthDetailDto
import co.brainz.itsm.auth.dto.AuthDto
import co.brainz.itsm.auth.dto.AuthMenuDto
import co.brainz.itsm.auth.dto.AuthUrlDto
import co.brainz.itsm.auth.repository.AuthRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authRepository: AuthRepository,
    private val roleAuthMapRepository: AliceRoleAuthMapRepository,
    private val menuRepository: AliceMenuRepository,
    private val menuAuthMapRepository: AliceMenuAuthMapRepository,
    private val urlRepository: AliceUrlRepository,
    private val urlAuthMapRepository: AliceUrlAuthMapRepository
) {

    /**
     * 전체 권한 목록 조회
     */
    fun getAuthList(): MutableList<AliceAuthEntity> {
        return authRepository.findByOrderByAuthNameAsc()
    }

    /**
     * 전체 메뉴 목록 조회
     */
    fun getMenuList(): MutableList<AliceMenuEntity> {
        return menuRepository.findByOrderByMenuIdAsc()
    }

    /**
     * 전체 url 목록 조회
     */
    fun getUrlList(): MutableList<AliceUrlEntity> {
        return urlRepository.findByOrderByUrlAsc()
    }

    /**
     * 권한 정보 삭제
     */
    fun deleteAuth(authId: String): String {
        val authInfo = authRepository.findByAuthId(authId)
        val menuAuthMapCount = menuAuthMapRepository.findByAuth(authInfo).count()
        val urlAuthMapCount = urlAuthMapRepository.findByAuth(authInfo).count()

        if (menuAuthMapCount > 0 || urlAuthMapCount > 0) {
            authInfo.menuAuthMapEntities.forEach { menuAuthMap ->
                menuAuthMapRepository.deleteById(AliceMenuAuthMapPk(menuAuthMap.menu.menuId, authId))
            }
            authInfo.urlAuthMapEntities.forEach { urlAuthMap ->
                val urlEntityPk = AliceUrlEntityPk(urlAuthMap.url.url, urlAuthMap.url.method)
                val urlAuthMapPk = AliceUrlAuthMapPk(urlEntityPk, authInfo.authId)
                urlAuthMapRepository.deleteById(urlAuthMapPk)
            }
        }

        authRepository.deleteById(authId)
        return "true"
    }

    /**
     * 권한 정보 등록
     */
    fun createAuth(authInfo: AuthDto): String {
        val auth = AliceAuthEntity(
            authId = authInfo.authId.toString(),
            authName = authInfo.authName.toString(),
            authDesc = authInfo.authDesc.toString()
        )
        val result = authRepository.save(auth)

        menuRepository.findByMenuIdIn(authInfo.arrMenuId!!).forEach { menu ->
            menuAuthMapRepository.save(AliceMenuAuthMapEntity(menu, auth))
        }
        urlRepository.findByUrlIn(authInfo.arrUrl!!).forEach { url ->
            urlAuthMapRepository.save(
                AliceUrlAuthMapEntity(
                    AliceUrlEntity(
                        url.url,
                        url.method,
                        url.urlDesc,
                        url.requiredAuth
                    ), auth
                )
            )
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

        authRepository.findByAuthId(auth.authId).menuAuthMapEntities.forEach { menuAuthMap ->
            menuAuthMapRepository.deleteById(AliceMenuAuthMapPk(menuAuthMap.menu.menuId, auth.authId))
        }
        menuRepository.findByMenuIdIn(authInfo.arrMenuId!!).forEach { menu ->
            menuAuthMapRepository.save(AliceMenuAuthMapEntity(menu, auth))
        }

        authRepository.findByAuthId(auth.authId).urlAuthMapEntities.forEach { urlAuthMap ->
            val urlEntityPk = AliceUrlEntityPk(urlAuthMap.url.url, urlAuthMap.url.method)
            val urlAuthMapPk = AliceUrlAuthMapPk(urlEntityPk, auth.authId)
            urlAuthMapRepository.deleteById(urlAuthMapPk)
        }
        urlRepository.findByUrlIn(authInfo.arrUrl!!).forEach { url ->
            urlAuthMapRepository.save(
                AliceUrlAuthMapEntity(
                    AliceUrlEntity(
                        url.url,
                        url.method,
                        url.urlDesc,
                        url.requiredAuth
                    ), auth
                )
            )
        }

        return result.authId
    }

    /**
     * 권한 상세 정보 조회
     */
    fun getDetailAuths(authId: String): List<AuthDto> {
        val dto = mutableListOf<AuthDto>()
        val authInfo = authRepository.findByAuthId(authId)
        val menuList = mutableListOf<AuthMenuDto>()
        val urlList = mutableListOf<AuthUrlDto>()
        val roleAuthMapCount = roleAuthMapRepository.findByAuth(authInfo).count()

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
                urlList,
                roleAuthMapCount
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
