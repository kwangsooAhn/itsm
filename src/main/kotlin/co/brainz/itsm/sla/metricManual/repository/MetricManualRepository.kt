/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.repository

import co.brainz.itsm.sla.metricManual.entity.MetricManualEntity
import java.time.LocalDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MetricManualRepository : JpaRepository<MetricManualEntity, String>, MetricManualRepositoryCustom {
    @Query("select sum(m.metricValue) from MetricManualEntity m where m.metricManualId = :metricManualId " +
        "and m.referenceDate >= :startDt and m.referenceDate <= :endDt")
    fun findManualPointSum(metricManualId: String, startDt: LocalDate, endDt: LocalDate): Float
}
