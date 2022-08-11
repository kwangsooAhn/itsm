/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.notification.dto

import java.io.Serializable

data class NotificationConfigDetailDto(
    val channel: String,
    val useYn: Boolean,
    val titleFormat: String,
    val messageFormat: String,
    val template: String?,
    val url: List<String>?
) : Serializable
