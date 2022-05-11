/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricPool.repository

import co.brainz.itsm.sla.metricPool.entity.MetricGroupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MetricGroupRepository : JpaRepository<MetricGroupEntity, String> {

    fun existsByMetricGroupName(metricGroupName: String): Boolean
}
