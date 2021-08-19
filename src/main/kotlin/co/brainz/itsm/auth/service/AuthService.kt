/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

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
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.auth.dto.AuthDto
import co.brainz.itsm.auth.dto.AuthListReturnDto
import co.brainz.itsm.auth.dto.AuthMenuDto
import co.brainz.itsm.auth.dto.AuthSearchCondition
import co.brainz.itsm.auth.dto.AuthUrlDto
import co.brainz.itsm.auth.repository.AuthRepository
import javax.transaction.Transactional
import kotlin.math.ceil
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
    fun getAuthList(): AuthListReturnDto {
        val authList = authRepository.findAuthSearch(
            AuthSearchCondition(""))
        return AuthListReturnDto(
            data = authList.results,
            paging = AlicePagingData(
                totalCount = authList.total,
                totalCountWithoutCondition = authRepository.count(),
                currentPageNum = 0L,
                totalPageNum = 0L,
                orderType = null
            )
        )
    }

    /**
     * 권한 정보 검색
     */
    fun getAuthSearchList(authSearchCondition: AuthSearchCondition): AuthListReturnDto {
        val queryResult = authRepository.findAuthSearch(authSearchCondition)

        return AuthListReturnDto(
            data = queryResult.results,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = authRepository.count(),
                currentPageNum = authSearchCondition.pageNum,
                totalPageNum = ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code
            )
        )
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
        return urlRepository.findByOrderByMethodAscUrlAsc()
    }

    /**
     * 권한 정보 삭제
     */
    @Transactional
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
    @Transactional
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
    @Transactional
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
    fun getAuthDetail(authId: String): List<AuthDto> {
        val dto = mutableListOf<AuthDto>()
        val authInfo = authRepository.findByAuthId(authId)
        val menuList = mutableListOf<AuthMenuDto>()
        val urlList = mutableListOf<AuthUrlDto>()
        val roleAuthMapCount = roleAuthMapRepository.countByAuth(authInfo)

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
                null,
                menuList,
                null,
                urlList,
                roleAuthMapCount
            )
        )
        return dto
    }
}
