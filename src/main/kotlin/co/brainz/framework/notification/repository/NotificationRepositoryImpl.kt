package co.brainz.framework.notification.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.notification.dto.NotificationDto
import co.brainz.framework.notification.entity.NotificationEntity
import co.brainz.framework.notification.entity.QNotificationEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class NotificationRepositoryImpl : QuerydslRepositorySupport(NotificationEntity::class.java),
    NotificationRepositoryCustom {

    override fun findNotificationList(receivedUser: String): List<NotificationDto> {
        val notification = QNotificationEntity.notificationEntity
        val user = QAliceUserEntity.aliceUserEntity
        return from(notification)
            .select(
                Projections.constructor(
                    NotificationDto::class.java,
                    notification.notificationId,
                    notification.receivedUser.userKey,
                    notification.title,
                    notification.message,
                    notification.instanceId,
                    notification.confirmYn,
                    notification.displayYn,
                    notification.createDt
                )
            )
            .innerJoin(notification.receivedUser, user)
            .where(notification.receivedUser.userId.eq(receivedUser))
            .orderBy(notification.confirmYn.asc(), notification.createDt.desc())
            .limit(AliceConstants.NOTIFICATION_SIZE)
            .fetch()
    }
}
