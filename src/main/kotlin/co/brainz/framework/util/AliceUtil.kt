package co.brainz.framework.util

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
}
