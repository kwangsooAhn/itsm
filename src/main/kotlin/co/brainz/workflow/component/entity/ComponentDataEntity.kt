package co.brainz.workflow.component.entity

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "wf_comp_data")
data class ComponentDataEntity(

        @Id
        @Column(name = "attr_id")
        val attrId: String,

        @Column(name = "attr_value")
        val attrValue: String,

        @Column(name = "attr_order")
        val attrOrder: Int,

        @Id
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "comp_id")
        val attributes: ComponentMstEntity

) : Serializable
