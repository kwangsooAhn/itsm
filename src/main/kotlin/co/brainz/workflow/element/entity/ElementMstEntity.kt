package co.brainz.workflow.element.entity

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.Column
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.FetchType

@Entity
@Table(name = "wf_elem_mst")
data class ElementMstEntity(
        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        @Column(name = "elem_id", length = 256)
        val elementId: String,

        @Column(name="proc_id", length = 128)
        val processId: String,

        @Column(name = "elem_type", length = 100)
        val elementType: String,

        @Column(name = "elem_name", length = 256)
        val elementName: String,

        @Column(name = "elem_desc", length = 1024)
        val elementDesc: String,

        @Column(name = "noti_email")
        val notificationEmail: Boolean,

        @Column(name = "elem_config")
        val elementConfig: String,

        @Column(name = "display_info")
        val displayInfo: String
): Serializable {
    @OneToMany(mappedBy = "element", fetch = FetchType.LAZY)
    val elementDataEntities = mutableListOf<ElementDataEntity>()

    fun getElementDataValue(elementAttributeId: String): String? {
        var elementAttributeValue: String? = null
        this.elementDataEntities.forEach { elementData ->
            if (elementData.attributeId == elementAttributeId) elementAttributeValue = elementData.attributeValue
        }
        return elementAttributeValue
    }
}