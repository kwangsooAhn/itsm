package co.brainz.framework.util

import co.brainz.framework.constants.AliceConstants

class AliceUtil {

    /**
     * URL 제외 패턴 확인.
     */
    fun urlExcludePatternCheck(requestUrl: String): Boolean {
        val result = AliceConstants.AccessAllowUrlPatten.getAccessAllowUrlPatten().find {
            if ("\\*\\*$".toRegex().containsMatchIn(it)) {
                requestUrl.startsWith(it.replace("**", ""))
            } else {
                requestUrl.contentEquals(it)
            }
        }.isNullOrBlank()
        return !result
    }
}