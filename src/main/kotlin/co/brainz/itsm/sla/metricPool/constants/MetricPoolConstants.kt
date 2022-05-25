/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricPool.constants

object MetricPoolConstants {

    /**
     * metric type p_code
     */
    const val METRIC_TYPE_P_CODE = "sla.metricType"

    /**
     * metric UNIT p_code
     */
    const val METRIC_UNIT_P_CODE = "sla.metricUnit"

    /**
     * metric calculation type p_code
     */
    const val METRIC_CALCULATION_TYPE_P_CODE = "sla.calculationType"

    /**
     * metric group p_code
     */
    const val METRIC_GROUP_P_CODE = "sla.metricGroup"

    enum class MetricTypeCode(val code: String) {
        AUTO("auto"),
        MANUAL("manual")

    }
}
