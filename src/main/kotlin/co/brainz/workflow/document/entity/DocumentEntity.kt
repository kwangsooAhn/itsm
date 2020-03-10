package co.brainz.workflow.document.entity

import co.brainz.workflow.form.entity.FormEntity
import co.brainz.workflow.process.entity.ProcessEntity
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
import javax.persistence.Table

@Entity
@Table(name = "wf_document")
data class DocumentEntity(

    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "document_id", length = 128)
    val documentId: String,

    @Column(name = "document_name", length = 128)
    val documentName: String,

    @Column(name = "document_desc", length = 256)
    val documentDesc: String?,

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
    val processes: ProcessEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    val forms: FormEntity

) : Serializable

