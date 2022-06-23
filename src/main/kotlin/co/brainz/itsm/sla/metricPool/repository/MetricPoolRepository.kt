/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricPool.repository

import co.brainz.itsm.sla.metricPool.entity.MetricPoolEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MetricPoolRepository : JpaRepository<MetricPoolEntity, String>, MetricPoolRepositoryCustom {

    fun existsByMetricName(metricName: String): Boolean

    fun findByMetricId(metricId: String): MetricPoolEntity
}
