package co.brainz.framework.notification.repository

import co.brainz.framework.notification.dto.NotificationDto

interface NotificationRepositoryCustom {
    fun findNotificationListExceptDocument(receivedUser: String): List<NotificationDto>
    fun findNotificationListForDocument(receivedUser: String): List<NotificationDto>
}
