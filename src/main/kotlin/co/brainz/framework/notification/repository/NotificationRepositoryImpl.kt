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
import co.brainz.itsm.notice.entity.QNoticeEntity
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import co.brainz.workflow.token.entity.QWfTokenEntity
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.JPQLQuery
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class NotificationRepositoryImpl : QuerydslRepositorySupport(NotificationEntity::class.java),
    NotificationRepositoryCustom {

    val notification: QNotificationEntity = QNotificationEntity.notificationEntity
    val user: QAliceUserEntity = QAliceUserEntity.aliceUserEntity
    val instance: QWfInstanceEntity = QWfInstanceEntity.wfInstanceEntity
    val token: QWfTokenEntity = QWfTokenEntity.wfTokenEntity

    override fun findNotificationListExceptDocument(receivedUser: String): List<NotificationDto> {
        return getNotificationListQuery(receivedUser)
            .where(notification.type.eq(NotificationConstants.Type.CMDB.code))
            .orderBy(notification.confirmYn.asc(), notification.createDt.desc())
            .limit(AliceConstants.NOTIFICATION_SIZE)
            .fetch()
    }

    override fun findNotificationListForDocument(receivedUser: String): List<NotificationDto> {
        val subToken = QWfTokenEntity.wfTokenEntity
        val startDtSubToken = QWfTokenEntity.wfTokenEntity

        return getNotificationListQuery(receivedUser)
            .innerJoin(instance).on(notification.instanceId.eq(instance.instanceId))
            .innerJoin(token).on(token.instance.instanceId.eq(instance.instanceId))
            .where(notification.type.eq(NotificationConstants.Type.DOCUMENT.code)
                // 최신 토큰값 조회를 위해 tokenId.max() 대신 tokenStartDt.max()로 수정 (#12080 참고)
                .and(token.tokenId.eq(
                    from(subToken)
                        .select(subToken.tokenId.max())
                        .where(subToken.tokenStartDt.eq(
                            from(startDtSubToken)
                                .select(startDtSubToken.tokenStartDt.max())
                                .where(startDtSubToken.instance.instanceId.eq(instance.instanceId))
                        ))
                ))
            )
            .orderBy(notification.confirmYn.asc(), notification.createDt.desc())
            .limit(AliceConstants.NOTIFICATION_SIZE)
            .fetch()
    }

    /**
     * 알림 공통 목록 조회
     */
    private fun getNotificationListQuery(receivedUser: String): JPQLQuery<NotificationDto> {
        return from(notification)
            .select(
                Projections.constructor(
                    NotificationDto::class.java,
                    notification.notificationId,
                    notification.receivedUser.userKey,
                    notification.title,
                    notification.message,
                    notification.instanceId,
                    ExpressionUtils.`as`(
                        JPAExpressions.select(token.tokenId).from(token)
                            .where(token.instance.instanceId.eq(notification.instanceId)), "tokenId"
                    ),
                    ExpressionUtils.`as`(
                        JPAExpressions.select(instance.documentNo).from(instance)
                            .where(instance.instanceId.eq(notification.instanceId)), "documentNo"
                    ),
                    //token.tokenId,
                    //instance.documentNo,
                    notification.confirmYn,
                    notification.displayYn,
                    notification.createDt,
                    notification.target
                )
            )
            .innerJoin(notification.receivedUser, user)
            //.leftJoin(instance).on(notification.instanceId.eq(instance.instanceId))
            //.leftJoin(token).on(token.instance.instanceId.eq(instance.instanceId))
            .where(notification.receivedUser.userId.eq(receivedUser)
                .and(notification.target.eq(NotificationConstants.ITSM_IDENTIFIER))
            )
    }
}
