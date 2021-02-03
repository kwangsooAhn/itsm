/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "awf_chart")
data class ChartEntity(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "chart_id", length = 128)
    var chartId: String = "",

    @Column(name = "chart_type", length = 128)
    var chartType: String = "",

    @Column(name = "chart_name", length = 256)
    var chartName: String = "",

    @Column(name = "chart_desc")
    var chartDesc: String? = null,

    @Column(name = "chart_config")
    var chartConfig: String = ""
) : Serializable, AliceMetaEntity()
