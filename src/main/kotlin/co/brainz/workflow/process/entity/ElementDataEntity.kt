package co.brainz.workflow.process.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "wf_elem_data")
@IdClass(ElementDataPk::class)
data class ElementDataEntity(
    @Id
    @Column(name = "elem_id", length = 256)
    val elemId: String = "",

    @Id
    @Column(name = "attr_id", length = 100)
    var attrId: String = "",

    @Id
    @Column(name = "attr_value", length = 512)
    var atrrValue: String = "",

    @Column(name = "attr_order")
    var attrOrder: Int? = 0

) : Serializable

data class ElementDataPk(
    val elemId: String = "",
    val attrId: String = "",
    val atrrValue: String = ""
) : Serializable
