package co.brainz.itsm.role.entity

import co.brainz.framework.menu.MenuEntity
import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GenerationType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column
import java.time.LocalDateTime
import javax.persistence.OneToMany
import javax.persistence.JoinTable
import co.brainz.framework.auth.entity.AuthEntity
import javax.persistence.Transient

@Entity
@Table(name = "awfRoleAuthMap")
public data class RoleAuthMapEntity(
	@Id
	@Column(name = "roleId") var roleId: String,
	@Column(name = "authId") var authId: String
) : Serializable