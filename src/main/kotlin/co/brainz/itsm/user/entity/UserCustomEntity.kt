package co.brainz.itsm.user.entity

import co.brainz.framework.auth.entity.AliceUserEntity
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
@Table(name = "awf_user_custom")
@IdClass(UserCustomEntityPk::class)
data class UserCustomEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    var user: AliceUserEntity,

    @Id
    @Column(name = "custom_type", length = 128)
    var customType: String,

    @Column(name = "custom_value", length = 512)
    var customValue: String? = ""
): Serializable

data class UserCustomEntityPk(
    var user: String = "",
    var customType: String = ""
) : Serializable