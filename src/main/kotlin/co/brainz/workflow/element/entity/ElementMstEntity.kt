package co.brainz.workflow.element.entity

import co.brainz.workflow.process.entity.ProcessMstEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "wf_elem_mst")
data class ElementMstEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "elem_id", length = 256)
    val elementId: String = "",

    @Column(name = "proc_id", length = 128)
    val processId: String = "",

    @Column(name = "elem_type", length = 100)
    val elementType: String = "",

    @Column(name = "elem_name", length = 256)
    val elementName: String = "",

    @Column(name = "elem_desc", length = 1024)
    val elementDesc: String = "",

    @Column(name = "noti_email")
    val notificationEmail: Boolean = false,

    @Column(name = "elem_config")
    val elementConfig: String = "",

    @Column(name = "display_info")
    val displayInfo: String = ""

) : Serializable {

    @OneToMany(
        mappedBy = "element",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE],
        orphanRemoval = true
    )
    val elementDataEntities: MutableList<ElementDataEntity> = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proc_id", insertable = false, updatable = false)
    var processMstEntity: ProcessMstEntity? = null

    fun getElementDataValue(elementAttributeId: String): String? {
        var elementAttributeValue: String? = null
        this.elementDataEntities.forEach { elementData ->
            if (elementData.attributeId == elementAttributeId) elementAttributeValue = elementData.attributeValue
        }
        return elementAttributeValue
    }
}
