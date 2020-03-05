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
@Table(name = "wf_comp_data")
@IdClass(ComponentDataPk::class)
data class ComponentDataEntity(

        @Id
        @Column(name = "comp_id", length = 128)
        val compId: String,

        @Id
        @Column(name = "attr_id", length = 100)
        val attrId: String,

        @Column(name = "attr_value", length = 512)
        val attrValue: String,

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "comp_id", nullable = false, insertable = false, updatable = false)
        val attributes: ComponentMstEntity

) : Serializable

data class ComponentDataPk(
        val compId: String = "",
        val attrId: String = ""
) : Serializable
