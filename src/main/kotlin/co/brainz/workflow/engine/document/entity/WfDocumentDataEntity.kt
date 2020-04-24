package co.brainz.workflow.engine.document.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "wf_document_data")
@IdClass(WfDocumentDataPkey::class)
data class WfDocumentDataEntity(

    @Id
    @Column(name = "document_id")
    val documentId: String,

    @Id
    @Column(name = "component_id")
    val componentId: String,

    @Id
    @Column(name = "element_id")
    val elementId: String,

    @Column(name = "display", length = 100)
    var display: String = "editable"

) : Serializable

data class WfDocumentDataPkey(
    var documentId: String = "",
    var componentId: String = "",
    var elementId: String = ""
) : Serializable
