/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.constants

object NumberingRuleConstants {

    /**
     * 문서 번호 편집 상태.
     */
    enum class Status(val code: String) {
        STATUS_SUCCESS("Z-0000"),
        STATUS_ERROR_RULE_USED("E-0004"),
        STATUS_ERROR_DUPLICATION("E-0001")
    }
}
