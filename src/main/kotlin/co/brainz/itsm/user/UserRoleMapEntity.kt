package co.brainz.itsm.user

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
    @Column(name = "userKey") var userKey: String,
    @Id
    @Column(name = "roleId") var roleId: String
) : Serializable
