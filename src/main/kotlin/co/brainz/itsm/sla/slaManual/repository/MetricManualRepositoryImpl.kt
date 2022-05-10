package co.brainz.itsm.sla.slaManual.repository

import co.brainz.itsm.sla.slaManual.entity.MetricManualEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class MetricManualRepositoryImpl : QuerydslRepositorySupport(MetricManualEntity::class.java), MetricManualRepositoryCustom {
}
