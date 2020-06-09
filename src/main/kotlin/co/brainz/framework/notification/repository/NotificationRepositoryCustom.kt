package co.brainz.framework.notification.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.notification.entity.NotificationEntity

interface NotificationRepositoryCustom {
    fun findNotificationList(receivedUser: AliceUserEntity): List<NotificationEntity>
}
