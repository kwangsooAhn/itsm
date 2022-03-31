/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CIClassNotificationEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface CIClassNotificationRepositoryCustom : AliceRepositoryCustom {
    fun findClassNotificationList(classId: String): List<CIClassNotificationEntity>
}
