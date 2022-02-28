/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */
package co.brainz.itsm.faq.constants

object FaqConstants {

    /**
     * faq category p_code
     */
    const val FAQ_CATEGORY_P_CODE = "faq.category"

    enum class Status(val code: String) {
        STATUS_SUCCESS("0"),
        STATUS_ERROR_DUPLICATION("1")
    }
}
