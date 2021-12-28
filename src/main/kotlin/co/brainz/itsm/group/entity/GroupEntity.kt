package co.brainz.itsm.group.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction

@Entity
@Table(name = "awf_group")
data class GroupEntity(
    @Id @Column(name = "group_id", length = 100)
    var groupId: String = "",

    @Column(name = "p_group_id", length = 100)
    var pGroupId: String = "",

    @Column(name = "group_name", length = 128)
    var groupName: String? = null,

    @Column(name = "group_desc", length = 128)
    var groupDesc: String? = null,

    @Column(name = "use_yn")
    var useYn: Boolean? = true,

    @Column(name = "level")
    var level: Int? = null,

    @Column(name = "seq_num")
    var seqNum: Int? = null,

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String? = null,

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @Column(name = "update_user_key", length = 128)
    var updateUserKey: String? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null

) : Serializable