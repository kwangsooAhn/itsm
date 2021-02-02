/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.constants

object ChartConstants {

    /**
     * chart group p_code.
     */
    const val CHART_TYPE_P_CODE = "chart.type"

    /**
     * chart operation p_code
     */
    const val CHART_OPERATION_P_CODE = "chart.operation"

    /**
     * chart unit p_code
     */
    const val CHART_UNIT_P_CODE = "chart.unit"

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
        PERIODUNIT("periodUnit"),
        GROUP("group")
    }
}
