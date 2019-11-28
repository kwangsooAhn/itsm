package co.brainz.itsm.role.entity

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column

@Entity
@Table(name = "awfUserRoleMap")
public data class UserRoleMapEntity(
	@Id
	@Column(name = "userId") var userId: String,
	@Column(name = "roleId") var roleId: String
) : Serializable {}