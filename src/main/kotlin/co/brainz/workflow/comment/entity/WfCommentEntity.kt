package co.brainz.workflow.comment.entity

import co.brainz.workflow.instance.entity.WfInstanceEntity
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
@Table(name = "wf_comment")
data class WfCommentEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "comment_id", length = 128)
    val commentId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id")
    var instance: WfInstanceEntity? = null,

    @Column(name = "content")
    val content: String? = null,

    @Column(name = "create_dt")
    val createDt: LocalDateTime,

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String = ""

) : Serializable
