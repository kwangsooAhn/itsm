/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.constants

object ReportConstants {

    object Template {
        /**
         * 템플릿 수정 상태
         */
        enum class EditStatus(val code: String) {
            STATUS_SUCCESS("0"),
            STATUS_ERROR_DUPLICATION("1"),
            STATUS_ERROR_NOT_EXIST("2")
        }
    }

    enum class ReportCreateStatus(val code: String) {
        STATUS_SUCCESS("0"),
        STAUTS_FAIL("1")
    }
}
