package co.brainz.workflow.component.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "wf_component_data")
@IdClass(ComponentDataPk::class)
data class ComponentDataEntity(

        @Id
        @Column(name = "component_id", length = 128)
        val componentId: String,

        @Id
        @Column(name = "attribute_id", length = 100)
        val attributeId: String,

        @Column(name = "attribute_value", length = 512)
        val attributeValue: String,

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "component_id", nullable = false, insertable = false, updatable = false)
        val attributes: ComponentEntity

) : Serializable

data class ComponentDataPk(
        val componentId: String = "",
        val attributeId: String = ""
) : Serializable
