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
        SUCCESS_EDIT("Z-0001"),
        SUCCESS_EDIT_EMAIL("Z-0002"),
        SUCCESS_EDIT_PASSWORD("Z-0003"),
        SUCCESS_ALREADY_LOGIN("Z-0004"),
        ERROR_FAIL("E-0000"),
        ERROR_DUPLICATE("E-0001"),
        ERROR_EXPIRED("E-0002"),
        ERROR_NOT_EXIST("E-0003"),
        ERROR_EXIST("E-0004"),
        ERROR_NOT_FOUND("E-0005"),
        ERROR_ACCESS_DENY("E-0006"),
        ERROR_DUPLICATE_EMAIL("E-0007"),
        ERROR_DUPLICATE_ORGANIZATION("E-0008"),
        ERROR_DUPLICATE_WORKFLOW("E-0009"),
        ERROR_NOT_EXIST_CLASS("E-0010"),
        ERROR_DUPLICATE_LOGIN("E-0011"),
        ERROR_INVALID_USER("E-0012"),
        ERROR_DISABLED_USER("E-0013"),
        ERROR_PROCESSED_TOKEN("E-0014")
    }
}
