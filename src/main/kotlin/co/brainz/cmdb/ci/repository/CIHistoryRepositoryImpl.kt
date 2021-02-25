/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIHistoryEntity
import co.brainz.cmdb.ci.entity.QCIHistoryEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIHistoryRepositoryImpl : QuerydslRepositorySupport(CIHistoryEntity::class.java), CIHistoryRepositoryCustom {
    override fun findByLatelyHistory(ciId: String): CIHistoryEntity? {
        val history = QCIHistoryEntity.cIHistoryEntity
        return from(history)
            .where(history.ciId.eq(ciId))
            .orderBy(history.seq.desc())
            .fetchFirst()
    }
}
