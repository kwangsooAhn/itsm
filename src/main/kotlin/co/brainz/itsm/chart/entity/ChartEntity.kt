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
    val chartId: String,

    @Column(name = "chart_type", length = 128)
    val chartType: String,

    @Column(name = "chart_name", length = 256)
    val chartName: String,

    @Column(name = "chart_desc")
    val chartDesc: String
) : Serializable, AliceMetaEntity()
