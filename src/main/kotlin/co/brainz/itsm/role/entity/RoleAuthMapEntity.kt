package co.brainz.itsm.role.entity

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column
import java.time.LocalDateTime

@Entity
@Table(name = "awfRoleAuthMap")
public data class RoleAuthMapEntity(
	@Id
	@Column(name = "roleId") var roleId: String,
	@Column(name = "authId") var authId: String
) : Serializable