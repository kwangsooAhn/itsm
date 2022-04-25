/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.response

object ZResponseConstants {

    /**
     * 상태 코드
     */
    enum class STATUS(val code: String) {
        SUCCESS("Z-0000"),
        ERROR_FAIL("E-0000"),
        ERROR_DUPLICATE("E-0001"),
        ERROR_EXPIRED("E-0002"),
        ERROR_NOT_EXIST("E-0003"),
        ERROR_EXIST("E-0004"),
        ERROR_NOT_FOUND("E-0005"),
        ERROR_ACCESS_DENY("E-0006")
    }
}
