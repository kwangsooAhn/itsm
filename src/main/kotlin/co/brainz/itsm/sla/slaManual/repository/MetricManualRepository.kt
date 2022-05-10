package co.brainz.itsm.sla.slaManual.repository

import co.brainz.itsm.sla.slaManual.entity.MetricManualEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MetricManualRepository: JpaRepository<MetricManualEntity, String>, MetricManualRepositoryCustom{
}
