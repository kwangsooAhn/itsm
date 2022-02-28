/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.dto

import java.io.Serializable

data class OrganizationItem(
    val title: String,
    val width: String,
    val type: String,
    val name: String,
    val dataType: String
) : Serializable
