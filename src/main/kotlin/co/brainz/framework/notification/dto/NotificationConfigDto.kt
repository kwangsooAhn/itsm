/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.notification.dto

import java.io.Serializable

data class NotificationConfigDto(
    var notificationCode: String,
    var notificationName: String,
    var notificationConfigDetails: MutableList<NotificationConfigDetailDto>? = mutableListOf()
) : Serializable
