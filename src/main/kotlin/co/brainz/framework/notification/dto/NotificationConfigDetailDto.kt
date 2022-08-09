/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.notification.dto

import java.io.Serializable

data class NotificationConfigDetailDto(
    var channel: String,
    var useYn: Boolean,
    var titleFormat: String,
    var messageFormat: String,
    var template: String?,
    var url: List<String>?
) : Serializable
