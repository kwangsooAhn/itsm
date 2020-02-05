package co.brainz.itsm.user.entity

import co.brainz.itsm.user.dto.UserRoleMapId
import java.io.Serializable
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.Column
import javax.persistence.IdClass

@Entity
@Table(name = "awf_user_role_map")
@IdClass(UserRoleMapId::class)
data public class UserRoleMapEntity(
    @Id
    @Column(name = "user_key") var userKey: String,
    @Id
    @Column(name = "role_id") var roleId: String
) : Serializable
