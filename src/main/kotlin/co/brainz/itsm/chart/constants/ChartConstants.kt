/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.constants

object ChartConstants {

    /**
     * chart p_code
     */
    enum class PCode(val code: String) {
        TYPE("chart.type"),
        OPERATION("chart.operation"),
        UNIT("chart.unit")
    }

    /**
     * chart status
     */
    enum class Status(val code: String) {
        STATUS_SUCCESS("0")
    }

    /**
     * chart Data Property
     */
    enum class ObjProperty(val property: String) {
        TYPE("type"),
        FROM("from"),
        OPERATION("operation"),
        DIGIT("digit"),
        UNIT("unit"),
        DURATION("duration"),
        PERIOD_UNIT("periodUnit"),
        GROUP("group")
    }

    /**
     * chart Type
     */
    enum class Type(val code: String) {
        STACKED_COLUMN("chart.stackedColumn"),
        BASIC_LINE("chart.basicLine"),
        PIE("chart.pie")
    }

    /**
     * chart Unit
     */
    enum class Unit(val code: String) {
        YEAR("Y"),
        MONTH("M"),
        DATE("D"),
        HOUR("H")
    }

    /**
     * chart Operation
     */
    enum class Operation(val code: String) {
        PERCENT("percent"),
        COUNT("count")
    }
}
