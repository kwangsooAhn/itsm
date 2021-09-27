/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */
package co.brainz.itsm.dashboard.dto

import java.io.Serializable

data class DashboardStatisticDto(
    var type: String = "",
    var total: Long = 0,
    var incident: Long = 0,
    var inquiry: Long = 0,
    var request: Long = 0,
    var etc: Long = 0
) : Serializable
