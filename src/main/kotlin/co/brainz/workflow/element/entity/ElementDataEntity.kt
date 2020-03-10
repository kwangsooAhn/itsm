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
@IdClass(ElementDataPk::class)
data class ElementDataEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id", insertable = false, updatable = false)
    val element: ElementEntity,

    @Id
    @Column(name = "attribute_id", length = 100)
    val attributeId: String = "",

    @Id
    @Column(name = "attribute_value", length = 512)
    var attributeValue: String = "",

    @Column(name = "attribute_order")
    var attributeOrder: Int? = 0

) : Serializable

data class ElementDataPk(
    val element: String = "",
    val attributeId: String = "",
    val attributeValue: String = ""
) : Serializable

