package co.brainz.itsm.role.entity

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column
import java.time.LocalDateTime
import javax.persistence.Inheritance

@Entity
@Table(name = "awfRole")
data public class RoleEntity(
	@Id
	@Column(name = "roleId") var roleId: String,
	@Column(name = "roleName") var roleName: String,
	@Column(name = "roleDesc") var roleDesc: String? = null,
	@Column(name = "createId") var createId: String? = null,
	@Column(name = "createDate") var createDate: LocalDateTime? = null,
	@Column(name = "updateId") var updateId: String? = null,
	@Column(name = "updateDate") var updateDate: LocalDateTime? = null
) : Serializable {}