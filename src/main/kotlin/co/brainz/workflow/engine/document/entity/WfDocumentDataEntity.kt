package co.brainz.workflow.engine.document.entity

import co.brainz.workflow.engine.component.entity.WfComponentEntity
import co.brainz.workflow.engine.element.entity.WfElementEntity
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
@Table(name = "wf_document_data")
@IdClass(WfDocumentDataPkey::class)
data class WfDocumentDataEntity(

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", insertable = false, updatable = false)
    val document: WfDocumentEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    val components: WfComponentEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id")
    val elements: WfElementEntity,

    @Column(name = "display", length = 100)
    var display: String = "editable"

) : Serializable

data class WfDocumentDataPkey(
    var documentId: String = "",
    var componentId: String = "",
    var elementId: String = ""
) : Serializable
