/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

/**
 * 연도별 SLA 지표관리 - 연도별 지표 등록, 수정 시 사용
 */
data class MetricYearData(
    val metricId: String,
    var year: String,
    var minValue: Float,
    var maxValue: Float,
    var weightValue: Float,
    var owner: String? = null,
    var comment: String? = null,
    var zqlString: String
) : Serializable

/**
 * 연도별 SLA 지표관리 - 목록 화면으로 데이터 전달 시 사용
 */
data class MetricYearViewData(
    val metricId: String,
    val metricYear: String,
    val metricGroupName: String,
    val metricName: String,
    val minValue: Float,
    val maxValue: Float,
    val weightValue: Float,
    val owner: String? = null,
    val comment: String? = null
) : Serializable
