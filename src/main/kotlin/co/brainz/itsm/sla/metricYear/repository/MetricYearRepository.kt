package co.brainz.itsm.sla.metricPool.repository

import co.brainz.itsm.sla.metricYear.entity.MetricYearEntity
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MetricYearRepository : JpaRepository<MetricYearEntity, String>, MetricYearRepositoryCustom
