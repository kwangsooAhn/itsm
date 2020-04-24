package co.brainz.workflow.engine.instance.entity

import co.brainz.workflow.engine.comment.entity.WfCommentEntity
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.folder.entity.WfFolderEntity
import co.brainz.workflow.engine.token.entity.WfTokenEntity
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
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "wf_instance")
data class WfInstanceEntity(

    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "instance_id", length = 128)
    val instanceId: String,

    @Column(name = "instance_status", length = 100)
    var instanceStatus: String,

    @Column(name = "instance_start_dt", nullable = false)
    val instanceStartDt: LocalDateTime? = null,

    @Column(name = "instance_end_dt", insertable = false)
    var instanceEndDt: LocalDateTime? = null,

    @Column(name = "instance_create_user_key", length = 128)
    var instanceCreateUserKey: String? = null,

    @Column(name = "p_token_id", length = 128)
    var pTokenId: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    val document: WfDocumentEntity

) : Serializable {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "instance")
    val tokens: MutableList<WfTokenEntity>? = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "instance")
    val folders: MutableList<WfFolderEntity>? = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "instance")
    val comments: MutableList<WfCommentEntity>? = mutableListOf()
}
