/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.notification.repository

import co.brainz.framework.notification.entity.NotificationDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationDataRepository : JpaRepository<NotificationDataEntity, String>, NotificationDataRepositoryCustom
