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

    // todo: 현재 조회하는 필드 일부는 임시로 추가한 dummy입니다. 추후 수정이 필요합니다.
    override fun findAllHistory(ciId: String): List<CIHistoryEntity> {
        val history = QCIHistoryEntity.cIHistoryEntity
        return from(history)
            .where(history.ciId.eq(ciId))
            .orderBy(history.seq.desc())
            .fetch()
    }
}
