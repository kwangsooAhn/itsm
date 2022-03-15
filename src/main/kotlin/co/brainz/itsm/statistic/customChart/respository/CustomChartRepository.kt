/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.respository

import co.brainz.itsm.statistic.customChart.entity.ChartEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CustomChartRepository : JpaRepository<ChartEntity, String>,
    CustomChartRepositoryCustom {
    fun findChartEntityByChartId(chartId: String): ChartEntity?
    fun existsByChartNameAndChartType(chartName: String, chartType: String): Boolean
}
