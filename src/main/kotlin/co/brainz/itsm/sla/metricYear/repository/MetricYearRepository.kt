/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.repository

import co.brainz.itsm.sla.metricYear.entity.MetricYearEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MetricYearRepository: JpaRepository<MetricYearEntity, String>, MetricYearRepositoryCustom
