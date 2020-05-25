package co.brainz.framework.notification.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.notification.entity.NotificationEntity
import co.brainz.framework.notification.entity.QNotificationEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NotificationRepositoryImpl : QuerydslRepositorySupport(NotificationEntity::class.java),
    NotificationRepositoryCustom {
    override fun findNotificationList(receivedUser: AliceUserEntity): List<NotificationEntity> {
        val notification = QNotificationEntity.notificationEntity
        val query = from(notification)

        query.where(notification.receivedUser.eq(receivedUser))
        query.orderBy(notification.confirmYn.asc())
            .orderBy(notification.createDt.desc())
        query.limit(50)

        return query.fetch()
    }
}