package co.brainz.workflow.engine.folder.entity

import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "wf_folder")
@IdClass(WfFolderPk::class)
data class WfFolderEntity(
    @Id
    @Column(name = "folder_id", length = 128)
    var folderId: String = "",

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instanceId")
    val instance: WfInstanceEntity,

    @Column(name = "related_type", length = 100)
    val relatedType: String = "",

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null,

    @Column(name = "update_user_key", length = 128)
    var updateUserKey: String? = null
) : Serializable

data class WfFolderPk(
    var folderId: String = "",
    var instance: String = ""
) : Serializable
