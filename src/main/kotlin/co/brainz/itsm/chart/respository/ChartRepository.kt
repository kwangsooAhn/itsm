/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.respository

import co.brainz.itsm.chart.entity.ChartEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ChartRepository : JpaRepository<ChartEntity, String>, ChartRepositoryCustom
