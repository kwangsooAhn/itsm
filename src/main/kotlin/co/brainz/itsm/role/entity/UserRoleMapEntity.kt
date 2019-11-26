package co.brainz.itsm.role.entity

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GenerationType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column
import java.time.LocalDateTime

@Entity
@Table(name = "awfUserRoleMap")
public data class UserRoleMapEntity(
	@Id
	@Column(name = "roleId") var roleId: String,
	@Column(name = "userId") var userId: String
) : Serializable