package co.brainz.itsm.role.entity

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column
import java.time.LocalDateTime
import javax.persistence.Inheritance
import javax.persistence.*

@Entity
@Table(name = "awfRoleAuthMap")
data public class RoleAuthMapEntity(
	@Id
	@Column(name = "authId") var authId: String,
	@Column(name = "roleId") var roleId: String
) : Serializable {}