/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIHistoryEntity
import co.brainz.cmdb.ci.entity.QCIHistoryEntity
import co.brainz.cmdb.dto.CIHistoryDto
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIHistoryRepositoryImpl : QuerydslRepositorySupport(CIHistoryEntity::class.java), CIHistoryRepositoryCustom {
    override fun findByLatelyHistory(ciId: String): CIHistoryEntity? {
        val history = QCIHistoryEntity.cIHistoryEntity
        return from(history)
            .where(history.ciId.eq(ciId))
            .orderBy(history.seq.desc())
            .fetchFirst()
    }

    override fun findAllHistory(ciId: String): List<CIHistoryDto> {
        val history = QCIHistoryEntity.cIHistoryEntity
        val instance = QWfInstanceEntity.wfInstanceEntity
        return from(history)
            .select(
                Projections.constructor(
                    CIHistoryDto::class.java,
                    history.ciId,
                    history.ciNo,
                    history.ciStatus,
                    history.instance?.instanceId,
                    history.instance?.documentNo,
                    history.instance?.instanceCreateUser?.userKey,
                    history.automatic,
                    history.applyDt
                )
            )
            .leftJoin(history.instance, instance)
            .where(history.ciId.eq(ciId))
            .orderBy(history.seq.desc())
            .fetch()
    }
}
