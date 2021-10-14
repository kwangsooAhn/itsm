/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.dto

import java.io.Serializable

data class DashboardSearchCondition(
    var userKey: String = "",
    var instanceStatus: List<String>? = emptyList(),
    var tokenStatus: List<String>? = emptyList(),
    var searchFromDt: String = "",
    var searchToDt: String = ""
) : Serializable
