/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.entity

import co.brainz.itsm.numberingRule.entity.NumberingRuleEntity
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.process.entity.WfProcessEntity
import java.io.Serializable
import java.time.LocalDateTime
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "wf_document")
data class WfDocumentEntity(

    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "document_id", length = 128)
    val documentId: String,

    @Column(name = "document_type", length = 20)
    var documentType: String,

    @Column(name = "document_name", length = 128)
    var documentName: String,

    @Column(name = "document_status", length = 100)
    var documentStatus: String? = null,

    @Column(name = "document_desc", length = 256)
    var documentDesc: String? = null,

    @Column(name = "document_color", length = 128)
    var documentColor: String?,

    @Column(name = "document_group", length = 100)
    var documentGroup: String? = null,

    @Column(name = "api_enable")
    var apiEnable: Boolean = false,

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null,

    @Column(name = "update_user_key", length = 128)
    var updateUserKey: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    var process: WfProcessEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    var form: WfFormEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "numbering_id")
    var numberingRule: NumberingRuleEntity,

    @Column(name = "document_icon", length = 100)
    var documentIcon: String? = null

) : Serializable {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    val instance: MutableList<WfInstanceEntity>? = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    val display: MutableList<WfDocumentDisplayEntity> = mutableListOf()

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "document")
    val documentLink: WfDocumentLinkEntity ? = null
}
