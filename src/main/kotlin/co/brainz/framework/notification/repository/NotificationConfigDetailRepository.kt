/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.notification.repository

import co.brainz.framework.notification.entity.NotificationConfigDetailEntity
import co.brainz.framework.notification.entity.NotificationConfigEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationConfigDetailRepository : JpaRepository<NotificationConfigDetailEntity, String>,
    NotificationConfigDetailRepositoryCustom {
    fun findByNotificationConfig(notificationConfig: NotificationConfigEntity): List<NotificationConfigDetailEntity>
}
