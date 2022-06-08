/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.constants

object CalendarConstants {

    /**
     * 출력 타입
     */
    enum class ViewType(val code: String) {
        DAY("day"),
        WEEK("week"),
        MONTH("month"),
        TASK("task")
    }

    /**
     * 반복 일정 기간
     */
    enum class RepeatPeriod(val code: String) {
        ALL("all"),
        TODAY("today"),
        AFTER("after")
    }

    /**
     * 반복 일정 타입
     */
    enum class RepeatType(val code: String) {
        WEEK_OF_MONTH("weekOfMonth"),
        DAY_OF_WEEK_IN_MONTH("dayOfMonth") // dayOfWeekInMonth
    }

    /**
     * 커스텀 타입
     */
    enum class CustomType(val code: String) {
        MODIFY("modify"),
        DELETE("delete")
    }
}
