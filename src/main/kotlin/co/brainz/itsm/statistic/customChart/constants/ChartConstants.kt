/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.constants

object ChartConstants {

    /**
     * chart p_code
     */
    enum class PCode(val code: String) {
        TYPE("chart.type"),
        OPERATION("chart.operation"),
        UNIT("chart.unit"),
        RANGE("chart.range"), // 차트 데이터 대상 기간
        DOCUMENT_STATUS("chart.documentStatus") // 차트 데이터 대상 문서 상태
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
        RANGE("range")
    }

    /**
     * chart Type
     */
    enum class Type(val code: String) {
        STACKED_COLUMN("chart.stackedColumn"),
        STACKED_BAR("chart.stackedBar"),
        BASIC_COLUMN("chart.basicColumn"),
        BASIC_LINE("chart.basicLine"),
        PIE("chart.pie")
    }

    /**
     * chart Unit
     */
    enum class Unit(val code: String) {
        YEAR("Y"),
        MONTH("M"),
        DAY("D"),
        HOUR("H")
    }

    /**
     * chart range type
     */
    enum class Range(val code: String) {
        BETWEEN("chart.range.between"),
        LAST_MONTH("chart.range.last.month"),
        LAST_DAY("chart.range.last.day"),
        ALL("chart.range.all"),
        NONE("chart.range.none")
    }

    /**
     * chart Operation
     */
    enum class Operation(val code: String) {
        PERCENT("percent"),
        COUNT("count"),
        AVERAGE("average")
    }

    /**
     * chart document status
     */
    enum class DocumentStatus(val code: String) {
        ONLY_FINISH("only.finish.document"),
        EVEN_RUNNING("even.running.document"),
        ONLY_RUNNING("only.running.document")
    }
}
