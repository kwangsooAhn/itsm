/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CICapacityEntity
import co.brainz.cmdb.ci.entity.QCICapacityEntity
import co.brainz.itsm.cmdb.ci.dto.CICapacityDto
import com.querydsl.core.types.Projections
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CICapacityRepositoryImpl : QuerydslRepositorySupport(CICapacityEntity::class.java), CICapacityRepositoryCustom {

    override fun findCapacityChartData(ciId: String): List<CICapacityDto> {
        val capacity: QCICapacityEntity = QCICapacityEntity.cICapacityEntity
        return from(capacity)
            .select(
                Projections.constructor(
                    CICapacityDto::class.java,
                    capacity.ci.ciId,
                    capacity.referenceDt,
                    capacity.memAvg,
                    capacity.cpuAvg,
                    capacity.diskAvg,
                    capacity.mappingId
                )
            )
            .where(capacity.ci.ciId.eq(ciId)
                .and(capacity.referenceDt.loe(LocalDateTime.now()).and(capacity.referenceDt.goe(LocalDateTime.now().minusDays(7L)))))
            .fetch()
    }
}
