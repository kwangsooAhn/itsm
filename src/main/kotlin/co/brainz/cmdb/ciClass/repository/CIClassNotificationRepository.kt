/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CIClassNotificationEntity
import co.brainz.cmdb.ciClass.entity.CIClassNotificationPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CIClassNotificationRepository : JpaRepository<CIClassNotificationEntity, CIClassNotificationPk>,
    CIClassNotificationRepositoryCustom {
    fun deleteCIClassNotificationEntitiesByCiClass_ClassId(classId: String)
}
