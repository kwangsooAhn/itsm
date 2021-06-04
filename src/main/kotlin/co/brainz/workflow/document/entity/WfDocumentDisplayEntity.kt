/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.entity

import co.brainz.workflow.document.constants.WfDocumentConstants
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
@Table(name = "wf_document_display")
@IdClass(WfDocumentDisplayPkey::class)
data class WfDocumentDisplayEntity(

    @Id
    @Column(name = "document_id")
    val documentId: String,

    @Id
    @Column(name = "form_group_id")
    val formGroupId: String,

    @Id
    @Column(name = "element_id")
    val elementId: String,

    @Column(name = "display", length = 100)
    val display: String = WfDocumentConstants.DisplayType.EDITABLE.value

) : Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", insertable = false, updatable = false)
    var document: WfDocumentEntity? = null
}

data class WfDocumentDisplayPkey(
    var documentId: String = "",
    var formGroupId: String = "",
    var elementId: String = ""
) : Serializable
