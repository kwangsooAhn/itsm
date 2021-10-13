/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.dto

import java.io.Serializable

data class DashboardGroupCountDto(
    val groupType: String,
    val count: Long
) : Serializable
