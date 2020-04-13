package co.brainz.workflow.engine.document.entity

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "wf_document_data")
@IdClass(WfDocumentDataPkey::class)
data class WfDocumentDataEntity(

    @Id
    @Column(name = "document_id", length = 128)
    val documentId: String,

    @Id
    @Column(name = "component_id", length = 128)
    val componentId: String,

    @Id
    @Column(name = "element_id", length = 256)
    val elementId: String,

    @Id
    @Column(name = "display", length = 100)
    var display: String = "editable"

) : Serializable

data class WfDocumentDataPkey(
        var documentId: String = "",
        var componentId: String = "",
        var elementId: String = ""
) : Serializable
