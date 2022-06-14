/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.dto

import java.io.Serializable

/**
 * SLA 지표관리 - 지표 등록, 수정 시 사용
 */
data class MetricData(
    val metricId: String,
    var metricName: String,
    var metricDesc: String? = null,
    var metricGroup: String,
    var metricType: String,
    var metricUnit: String,
    var calculationType: String
) : Serializable

/**
 * SLA 지표관리 - 목록 화면으로 데이터 전달 시 사용
 */
data class MetricViewData(
    val metricId: String,
    val metricName: String,
    val metricDesc: String? = null,
    val metricGroupName: String,
    val metricTypeName: String,
    val metricUnitName: String,
    val calculationTypeName: String
) : Serializable
