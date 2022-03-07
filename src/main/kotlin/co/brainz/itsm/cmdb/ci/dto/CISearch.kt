/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.cmdb.ci.dto

import java.io.Serializable

data class CISearch(
    val searchItems: List<CISearchAttribute>? = emptyList()
) : Serializable
