package co.brainz.framework.notification.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.notification.entity.NotificationEntity
import co.brainz.framework.notification.entity.QNotificationEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class NotificationRepositoryImpl : QuerydslRepositorySupport(NotificationEntity::class.java),
    NotificationRepositoryCustom {
    override fun findNotificationList(receivedUser: AliceUserEntity): List<NotificationEntity> {
        val notification = QNotificationEntity.notificationEntity
        return from(notification)
            .innerJoin(notification.receivedUser)
            .fetchJoin()
            .where(notification.receivedUser.eq(receivedUser))
            .orderBy(notification.confirmYn.asc())
            .orderBy(notification.createDt.desc())
            .limit(50)
            .fetch()
    }
}
