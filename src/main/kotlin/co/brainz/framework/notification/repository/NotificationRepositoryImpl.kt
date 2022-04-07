/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.notification.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.notification.constants.NotificationConstants
import co.brainz.framework.notification.dto.NotificationDto
import co.brainz.framework.notification.entity.NotificationEntity
import co.brainz.framework.notification.entity.QNotificationEntity
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import co.brainz.workflow.token.entity.QWfTokenEntity
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class NotificationRepositoryImpl : QuerydslRepositorySupport(NotificationEntity::class.java),
    NotificationRepositoryCustom {

    override fun findNotificationListExceptDocument(receivedUser: String): List<NotificationDto> {
        val notification: QNotificationEntity = QNotificationEntity.notificationEntity
        val user: QAliceUserEntity = QAliceUserEntity.aliceUserEntity

        return from(notification)
            .select(
                Projections.constructor(
                    NotificationDto::class.java,
                    notification.notificationId,
                    notification.receivedUser.userKey,
                    notification.title,
                    notification.message,
                    notification.instanceId,
                    Expressions.asString(""),
                    Expressions.asString(""),
                    notification.confirmYn,
                    notification.displayYn,
                    notification.createDt,
                    notification.type
                )
            )
            .innerJoin(notification.receivedUser, user)
            .where(notification.receivedUser.userId.eq(receivedUser)
                .and(notification.target.eq(NotificationConstants.ITSM_IDENTIFIER))
                .and(notification.type.notIn(NotificationConstants.Type.DOCUMENT.code))
            )
            .orderBy(notification.confirmYn.asc(), notification.createDt.desc())
            .limit(AliceConstants.NOTIFICATION_SIZE)
            .fetch()
    }

    override fun findNotificationListForDocument(receivedUser: String): List<NotificationDto> {
        val notification = QNotificationEntity.notificationEntity
        val user = QAliceUserEntity.aliceUserEntity
        val instance = QWfInstanceEntity.wfInstanceEntity
        val token = QWfTokenEntity.wfTokenEntity
        val subToken = QWfTokenEntity.wfTokenEntity
        val startDtSubToken = QWfTokenEntity.wfTokenEntity

        return from(notification)
            .select(
                Projections.constructor(
                    NotificationDto::class.java,
                    notification.notificationId,
                    notification.receivedUser.userKey,
                    notification.title,
                    notification.message,
                    notification.instanceId,
                    token.tokenId,
                    instance.documentNo,
                    notification.confirmYn,
                    notification.displayYn,
                    notification.createDt,
                    notification.type
                )
            )
            .innerJoin(notification.receivedUser, user)
            .innerJoin(instance).on(notification.instanceId.eq(instance.instanceId))
            .innerJoin(token).on(token.instance.instanceId.eq(instance.instanceId))
            .where(notification.receivedUser.userId.eq(receivedUser)
                // 최신 토큰값 조회를 위해 tokenId.max() 대신 tokenStartDt.max()로 수정 (#12080 참고)
                .and(token.tokenId.eq(
                    from(subToken)
                        .select(subToken.tokenId.max())
                        .where(subToken.tokenStartDt.eq(
                            from(startDtSubToken)
                                .select(startDtSubToken.tokenStartDt.max())
                                .where(startDtSubToken.instance.instanceId.eq(instance.instanceId))
                        )))
                ).and(
                    notification.target.eq(NotificationConstants.ITSM_IDENTIFIER)
                ).and(
                    notification.type.eq(NotificationConstants.Type.DOCUMENT.code)
                )
            )
            .orderBy(notification.confirmYn.asc(), notification.createDt.desc())
            .limit(AliceConstants.NOTIFICATION_SIZE)
            .fetch()
    }
}
