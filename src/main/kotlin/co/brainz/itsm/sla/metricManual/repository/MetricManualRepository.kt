package co.brainz.itsm.sla.metricManual.repository

import co.brainz.itsm.sla.metricManual.entity.MetricManualEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MetricManualRepository : JpaRepository<MetricManualEntity, String>, MetricManualRepositoryCustom
