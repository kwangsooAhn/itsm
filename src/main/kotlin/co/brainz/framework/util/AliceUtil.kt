/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.util

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.configuration.AliceApplicationRunner
import co.brainz.framework.constants.AliceConstants
import co.brainz.itsm.code.dto.CodeDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.util.UUID
import javax.servlet.http.HttpServletRequest

class AliceUtil {

    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

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
     * 로그인시에 받은 사용자 정보[AliceUserAuthDto]를 받아서 url, menu, avatar Path 를 더해서 [AliceUserDto]를 반환한다.
     */
    fun setUserDetails(aliceUser: AliceUserAuthDto): AliceUserDto {
        return AliceUserDto(
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
            aliceUser.grantedAuthorises,
            aliceUser.menus,
            aliceUser.urls,
            aliceUser.timezone,
            aliceUser.lang,
            aliceUser.timeFormat,
            aliceUser.theme,
            aliceUser.avatarPath
        )
    }

    /**
     * 전달받은 코드 리스트에서 특정 코드를 찾은 후, 해당 코드들을 리턴한다.
     */
    fun getCodes(codeList: MutableList<CodeDto>, searchCode: String): MutableList<CodeDto> {
        val codes = mutableListOf<CodeDto>()
        codeList.forEach {
            when (it.pCode == searchCode) {
                true -> {
                    codes.add(it)
                }
            }
        }
        return codes
    }

    /**
     * UUID 생성.
     */
    fun getUUID(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    /**
     * 에러를 문자열로 변환.
     */
    fun printStackTraceToString(throwable: Throwable): String {
        val sb = StringBuffer()
        try {
            sb.append(throwable.toString())
            sb.append("\n")
            val element = throwable.stackTrace
            for (idx in element.indices) {
                sb.append("\tat ")
                sb.append(element[idx].toString())
                sb.append("\n")
            }
        } catch (ex: java.lang.Exception) {
            return throwable.toString()
        }
        return sb.toString()
    }

    /**
     * String -> LinkedHashMap 변환
     *
     */
    fun convertStringToLinkedHashMap(srcString: Any?): LinkedHashMap<String, Any> {
        val linkedMapType = TypeFactory.defaultInstance()
            .constructMapType(LinkedHashMap::class.java, String::class.java, Any::class.java)
        var resultLinkedHashMap: LinkedHashMap<String, Any> = linkedMapOf()

        srcString?.let {
            resultLinkedHashMap = mapper.convertValue(srcString, linkedMapType)
        }
        return resultLinkedHashMap
    }
}
