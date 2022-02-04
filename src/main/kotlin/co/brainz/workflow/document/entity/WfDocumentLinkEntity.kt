/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "wf_document_link")
data class WfDocumentLinkEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "document_link_id", length = 128)
    val documentLinkId: String,

    @Column(name = "document_name", length = 128)
    var documentName: String,

    @Column(name = "document_desc", length = 256)
    var documentDesc: String? = null,

    @Column(name = "document_status", length = 100)
    var documentStatus: String? = null,

    @Column(name = "document_link_url", length = 100)
    var documentLinkUrl: String,

    @Column(name = "document_color", length = 128)
    var documentColor: String?,

    @Column(name = "document_icon", length = 100)
    var documentIcon: String? = null,

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null,

    @Column(name = "update_user_key", length = 128)
    var updateUserKey: String? = null
) : Serializable
