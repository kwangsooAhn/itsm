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
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.user.constants.UserConstants
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
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
    private val urlAuthMapRepository: AliceUrlAuthMapRepository,
    private val codeService: CodeService
) {

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * ?????? ?????? ?????? ??????
     */
    fun getAuthList(): AuthListReturnDto {
        val authList = authRepository.findAuthSearch(
            AuthSearchCondition("")
        )
        return AuthListReturnDto(
            data = mapper.convertValue(authList.dataList),
            paging = AlicePagingData(
                totalCount = authList.totalCount,
                totalCountWithoutCondition = authRepository.count(),
                currentPageNum = 0L,
                totalPageNum = 0L,
                orderType = null
            )
        )
    }

    /**
     * ?????? ?????? ??????
     */
    fun getAuthSearchList(authSearchCondition: AuthSearchCondition): AuthListReturnDto {
        val pagingResult = authRepository.findAuthSearch(authSearchCondition)

        return AuthListReturnDto(
            data = mapper.convertValue(pagingResult.dataList),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = authRepository.count(),
                currentPageNum = authSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / authSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code
            )
        )
    }

    /**
     * ?????? ?????? ?????? ??????
     */
    fun getMenuList(): MutableList<AliceMenuEntity> {
        return menuRepository.findByOrderByMenuIdAsc()
    }

    /**
     * ?????? url ?????? ??????
     */
    fun getUrlList(): MutableList<AliceUrlEntity> {
        return urlRepository.findByOrderByMethodAscUrlAsc()
    }

    /**
     * ?????? ?????? ??????
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
     * ?????? ?????? ??????
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

        if (authInfo.arrUrlList != null) {
            val authUrlAuthMapList = mutableListOf<AliceUrlAuthMapEntity>()
            val urlEntityList = urlRepository.findAll()
            authInfo.arrUrlList?.forEach { arr ->
                urlEntityList.forEach { urlEntity ->
                    if (urlEntity.url == arr.url && urlEntity.method == arr.method) {
                        authUrlAuthMapList.add(
                            AliceUrlAuthMapEntity(
                                auth = auth,
                                url = urlEntity
                            )
                        )
                    }
                }
            }
            if (authUrlAuthMapList.isNotEmpty()) {
                urlAuthMapRepository.saveAll(authUrlAuthMapList)
            }
        }

        return result.authId
    }

    /**
     * ?????? ?????? ??????
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

        if (authInfo.arrUrlList != null) {
            val authUrlAuthMapList = mutableListOf<AliceUrlAuthMapEntity>()
            val urlEntityList = urlRepository.findAll()
            authInfo.arrUrlList?.forEach { arr ->
                urlEntityList.forEach { urlEntity ->
                    if (urlEntity.url == arr.url && urlEntity.method == arr.method) {
                        authUrlAuthMapList.add(
                            AliceUrlAuthMapEntity(
                                auth = auth,
                                url = urlEntity
                            )
                        )
                    }
                }
            }
            if (authUrlAuthMapList.isNotEmpty()) {
                urlAuthMapRepository.saveAll(authUrlAuthMapList)
            }
        }

        return result.authId
    }

    /**
     * ?????? ?????? ?????? ??????
     */
    fun getAuthDetail(authId: String): AuthDto {
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

        return AuthDto(
            authInfo.authId,
            authInfo.authName,
            authInfo.authDesc,
            null,
            menuList,
            urlList,
            roleAuthMapCount
        )
    }

    /**
     * ?????? URL ?????? ??????
     */
    fun getDefaultUrlList(): List<AuthUrlDto> {
        val defaultUrlList = mutableListOf<AuthUrlDto>()
        val defaultCodeList = codeService.selectCodeByParent(UserConstants.DefaultUrl.USER_DEFAULT_URL.code)
        defaultCodeList.forEach { defaultCode ->
            val methodCodeList = codeService.selectCodeByParent(defaultCode.code)
            methodCodeList.forEach { methodCode ->
                defaultUrlList.add(
                    AuthUrlDto(
                        method = methodCode.codeValue,
                        url = defaultCode.codeValue
                    )
                )
            }
        }

        return defaultUrlList
    }
}
