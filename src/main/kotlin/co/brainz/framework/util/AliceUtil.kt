package co.brainz.framework.util

import co.brainz.framework.constants.AliceConstants

class AliceUtil {

    /**
     * URL 제외 패턴 확인.
     */
    fun urlExcludePatternCheck(requestUrl: String): Boolean {

        //항상허용된 URL인지 체크한다.
        var isPermit = false

        if (!isPermit) {
            isPermit = !AliceConstants.AccessAllowUrlPatten.getAccessAllowUrlPatten().find {
                if ("\\*\\*$".toRegex().containsMatchIn(it)) {
                    requestUrl.startsWith(it.replace("**", ""))
                } else {
                    requestUrl.contentEquals(it)
                }
            }.isNullOrBlank()
        }

        return isPermit

        /*val result = AliceConstants.AccessAllowUrlPatten.getAccessAllowUrlPatten().find {
            if ("\\*\\*$".toRegex().containsMatchIn(it)) {
                requestUrl.startsWith(it.replace("**", ""))
            } else {
                requestUrl.contentEquals(it)
            }
        }.isNullOrBlank()*/

        //위에 데이터가 존재하면.. false



        //return !result
    }
}
