package co.brainz.workflow.engine.document.entity

import co.brainz.framework.numbering.entity.AliceNumberingRuleEntity
import co.brainz.workflow.engine.form.entity.WfFormEntity
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import co.brainz.workflow.engine.process.entity.WfProcessEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.time.LocalDateTime
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
@Table(name = "wf_document")
data class WfDocumentEntity(

    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "document_id", length = 128)
    val documentId: String,

    @Column(name = "document_name", length = 128)
    var documentName: String,

    @Column(name = "document_status", length = 100)
    var documentStatus: String? = null,

    @Column(name = "document_desc", length = 256)
    var documentDesc: String?,

    @Column(name = "document_color", length = 128)
    var documentColor: String?,

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
    var numberingRule: AliceNumberingRuleEntity

) : Serializable {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
    val instance: MutableList<WfInstanceEntity>? = mutableListOf()
}
