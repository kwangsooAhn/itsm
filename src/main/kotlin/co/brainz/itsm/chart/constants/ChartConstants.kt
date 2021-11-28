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
        UNIT("chart.unit"),
        RANGE("chart.range") // 차트 데이터 대상 기간
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
        OPERATION("operation"),
        DIGIT("digit"),
        UNIT("unit"),
        RANGE("range"),
        PERIOD_UNIT("periodUnit"),
        GROUP("group")
    }

    /**
     * chart Type
     */
    enum class Type(val code: String) {
        STACKED_COLUMN("chart.stackedColumn"),
        STACKED_BAR("chart.stackedBar"),
        BASIC_LINE("chart.basicLine"),
        PIE("chart.pie"),
        LINE_AND_COLUMN("chart.lineAndColumn"),
        GAUGE("chart.gauge")
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
     * chart range type
     */
    enum class RangeType(val code: String) {
        BETWEEN("chart.range.between"),
        LAST_MONTH("chart.range.last.month"),
        LAST_DAY("chart.range.last.day"),
        ALL("chart.range.all")
    }

    /**
     * chart Operation
     */
    enum class Operation(val code: String) {
        PERCENT("percent"),
        COUNT("count"),
        AVERAGE("average")
    }
}
