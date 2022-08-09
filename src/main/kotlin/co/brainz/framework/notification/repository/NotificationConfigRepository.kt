/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.notification.repository

import co.brainz.framework.notification.entity.NotificationConfigEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationConfigRepository : JpaRepository<NotificationConfigEntity, String>, NotificationConfigRepositoryCustom {
    fun findByNotificationCode(notificationCode: String): NotificationConfigEntity
}
