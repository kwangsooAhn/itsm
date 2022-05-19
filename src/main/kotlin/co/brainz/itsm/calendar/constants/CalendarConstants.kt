/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.constants

object CalendarConstants {

    enum class ViewType(val code: String) {
        DAY("day"),
        WEEK("week"),
        MONTH("month")
    }

    enum class RepeatPeriod(val code: String) {
        ALL("all"),
        THIS("this"),
        AFTER("after")
    }
}
