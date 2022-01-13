/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.notification.dto

import java.io.Serializable
import java.time.LocalDateTime

data class NotificationDto(
    var notificationId: String = "",
    var receivedUser: String = "",
    var title: String = "",
    var message: String? = null,
    var instanceId: String? = null,
    var tokenId: String? = null,
    var documentNo: String? = null,
    var confirmYn: Boolean? = false,
    var displayYn: Boolean? = false,
    var createDt: LocalDateTime? = null
) : Serializable
