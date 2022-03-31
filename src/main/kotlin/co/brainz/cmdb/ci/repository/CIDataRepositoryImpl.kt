/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIDataEntity
import co.brainz.cmdb.ci.entity.QCIDataEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIDataRepositoryImpl : QuerydslRepositorySupport(CIDataEntity::class.java), CIDataRepositoryCustom {
    override fun findCIDataList(ciIds: Set<String>): List<CIDataEntity> {
        val ciDataEntity = QCIDataEntity.cIDataEntity
        return from(ciDataEntity)
            .where(ciDataEntity.ci.ciId.`in`(ciIds))
            .fetch()
    }
}
