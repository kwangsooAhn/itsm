package co.brainz.itsm.sla.metricPool.repository

import co.brainz.itsm.sla.metricPool.entity.SlaMetricEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SlaMetricPoolRepository : JpaRepository<SlaMetricEntity, String>, SlaMetricPoolRepositoryCustom {

}
