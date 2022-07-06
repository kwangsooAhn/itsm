/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.dto

import java.io.Serializable

data class CIComponentInfo(
    val ciId: String,
    val componentId: String,
    val instanceId: String,
    val interlink: String
) : Serializable
