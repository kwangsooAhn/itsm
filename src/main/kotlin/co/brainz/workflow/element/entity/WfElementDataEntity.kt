package co.brainz.workflow.element.entity

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
@Table(name = "wf_element_data")
@IdClass(WfElementDataPk::class)
data class WfElementDataEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id", insertable = false, updatable = false)
    val element: WfElementEntity,

    @Id
    @Column(name = "attribute_id", length = 100)
    val attributeId: String = "",

    @Id
    @Column(name = "attribute_value", length = 1024)
    var attributeValue: String = "",

    @Column(name = "attribute_order")
    var attributeOrder: Int? = 0,

    @Column(name = "attribute_required")
    var attributeRequired: Boolean = false

) : Serializable

data class WfElementDataPk(
    val element: String = "",
    val attributeId: String = "",
    val attributeValue: String = ""
) : Serializable
