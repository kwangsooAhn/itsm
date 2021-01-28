package co.brainz.cmdb.ci.entity

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "wf_component_ci_data")
@IdClass(CIComponentDataPk::class)
data class CIComponentDataEntity(

    @Id
    @Column(name = "ci_id", length = 128)
    val ciId: String,

    @Id
    @Column(name = "component_id", length = 128)
    val componentId: String,

    @Column(name = "values")
    var values: String,

    @Column(name = "instance_id", length = 128)
    var instanceId: String? = null

) : Serializable

data class CIComponentDataPk(
        val ciId: String = "",
        val componentId: String = ""
) : Serializable