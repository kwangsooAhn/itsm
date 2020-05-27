package co.brainz.workflow.instance.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.workflow.comment.entity.WfCommentEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.folder.entity.WfFolderEntity
import co.brainz.workflow.token.entity.WfTokenEntity
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_create_user_key")
    var instanceCreateUser: AliceUserEntity? = null,

    @Column(name = "p_token_id", length = 128)
    var pTokenId: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    val document: WfDocumentEntity,

    @Column(name = "document_no", length = 128)
    var documentNo: String? = null

) : Serializable {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "instance")
    var tokens: MutableList<WfTokenEntity>? = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "instance")
    var folders: MutableList<WfFolderEntity>? = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "instance")
    var comments: MutableList<WfCommentEntity>? = mutableListOf()
}
