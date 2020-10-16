package co.brainz.framework.notification.repository

import co.brainz.framework.notification.dto.NotificationDto

interface NotificationRepositoryCustom {
    fun findNotificationList(receivedUser: String): List<NotificationDto>
}
