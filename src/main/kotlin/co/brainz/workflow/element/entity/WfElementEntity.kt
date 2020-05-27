package co.brainz.workflow.element.entity

import co.brainz.workflow.process.entity.WfProcessEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import java.io.Serializable
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "wf_element")
data class WfElementEntity(
    @Id
    @Column(name = "element_id", length = 256)
    val elementId: String = "",

    @Column(name = "process_id", length = 128)
    val processId: String = "",

    @Column(name = "element_type", length = 100)
    val elementType: String = "",

    @Column(name = "element_name", length = 256)
    val elementName: String = "",

    @Column(name = "element_desc", length = 1024)
    val elementDesc: String = "",

    @Column(name = "notify_email")
    val notificationEmail: Boolean = false,

    @Column(name = "element_config")
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
    val elementDataEntities: MutableList<WfElementDataEntity> = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "element")
    val tokens: MutableList<WfTokenEntity>? = mutableListOf()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id", insertable = false, updatable = false)
    var processEntity: WfProcessEntity? = null

    fun getElementDataValue(elementAttributeId: String): String? {
        var elementAttributeValue: String? = null
        this.elementDataEntities.forEach { elementData ->
            if (elementData.attributeId == elementAttributeId) elementAttributeValue = elementData.attributeValue
        }
        return elementAttributeValue
    }
}
