/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.util

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.configuration.AliceApplicationRunner
import co.brainz.framework.constants.AliceConstants
import javax.servlet.http.HttpServletRequest

class AliceUtil {

    /**
     * URL Required Auth Check.
     *
     * @param requestUrl
     * @param requestMethod
     * @return Boolean
     */
    private fun urlRequiredAuthCheck(requestUrl: String, requestMethod: String): Boolean {
        var isPermission = false
        val regex = "\\{([a-zA-Z]*)}".toRegex()
        val requestUrls = requestUrl.split("/")
        AliceApplicationRunner.aliceUrls.let letDto@{ urlList ->
            urlList.forEach forEachList@{
                val urls = it.url.split("/")
                if (requestUrls.size == urls.size && it.method == requestMethod) {
                    requestUrls.forEachIndexed { index, url ->
                        if (urls[index] == url || regex.containsMatchIn(urls[index])) {
                            if (index == requestUrls.size - 1) {
                                isPermission = true
                                return@letDto
                            }
                        } else {
                            return@forEachList
                        }
                    }
                }
            }
        }
        if (!isPermission) {
            isPermission = !AliceApplicationRunner.aliceUrls.find {
                if ("\\*\\*$".toRegex().containsMatchIn(it.url)) {
                    requestUrl.startsWith(it.url.replace("**", ""))
                } else {
                    requestUrl.contentEquals(it.url)
                }
            }?.url.isNullOrBlank()
        }

        return isPermission
    }

    /**
     * URL 제외 패턴 확인.
     */
    fun urlExcludePatternCheck(request: HttpServletRequest): Boolean {
        val requestUrl = request.requestURI ?: ""
        val requestMethod = request.method.toLowerCase()
        var isPermission = urlRequiredAuthCheck(requestUrl, requestMethod)
        if (!isPermission) {
            isPermission = !AliceConstants.AccessAllowUrlPatten.getAccessAllowUrlPatten().find {
                if ("\\*\\*$".toRegex().containsMatchIn(it)) {
                    requestUrl.startsWith(it.replace("**", ""))
                } else {
                    requestUrl.contentEquals(it)
                }
            }.isNullOrBlank()
        }

        return isPermission
    }

    /**
     * 로그인시에 받은 사용자 정보[AliceUserAuthDto]를 받아서 url, menu, avatarPath를 더해서 [AliceUserDto]를 반환한다.
     */
    fun setUserDetails(aliceUser: AliceUserAuthDto): AliceUserDto? {
        return aliceUser.grantedAuthorises?.let { grantedAuthorises ->
            aliceUser.urls?.let { urls ->
                aliceUser.menus?.let { menus ->
                    aliceUser.avatarPath?.let { avatarPath ->
                        AliceUserDto(
                            aliceUser.userKey,
                            aliceUser.userId,
                            aliceUser.userName,
                            aliceUser.email,
                            aliceUser.position,
                            aliceUser.department,
                            aliceUser.officeNumber,
                            aliceUser.mobileNumber,
                            aliceUser.useYn,
                            aliceUser.tryLoginCount,
                            aliceUser.expiredDt,
                            aliceUser.oauthKey,
                            grantedAuthorises,
                            menus,
                            urls,
                            aliceUser.timezone,
                            aliceUser.lang,
                            aliceUser.timeFormat,
                            aliceUser.theme,
                            avatarPath
                        )
                    }
                }
            }
        }
    }
}
