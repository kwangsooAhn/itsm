/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReport.constants

object CustomReportConstants {

    object Template {
        /**
         * 템플릿 수정 상태
         */
        enum class EditStatus(val code: String) {
            STATUS_SUCCESS("Z-0000"),
            STATUS_ERROR_DUPLICATION("E-0001"),
            STATUS_ERROR_NOT_EXIST("E-0000")
        }
    }

    enum class ReportCreateStatus(val code: String) {
        STATUS_SUCCESS("Z-0000"),
        STAUTS_FAIL("E-0000")
    }
}
