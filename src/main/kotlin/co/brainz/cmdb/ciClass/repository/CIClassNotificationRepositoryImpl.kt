/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CIClassNotificationEntity
import co.brainz.cmdb.ciClass.entity.QCIClassNotificationEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIClassNotificationRepositoryImpl : QuerydslRepositorySupport(
    CIClassNotificationEntity::class.java
), CIClassNotificationRepositoryCustom {
    override fun findClassNotificationList(classId: String): List<CIClassNotificationEntity> {
        val ciClassNotification = QCIClassNotificationEntity.cIClassNotificationEntity
        return from(ciClassNotification)
            .where(ciClassNotification.ciClass.classId.eq(classId))
            .orderBy(ciClassNotification.attributeOrder.asc())
            .fetch()
    }
}
