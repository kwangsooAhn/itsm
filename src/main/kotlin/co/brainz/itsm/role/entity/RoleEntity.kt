package co.brainz.itsm.role.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Inheritance
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import javax.persistence.*
import co.brainz.itsm.user.entity.UserEntity

@Entity
@Table(name = "awfRole")
data public class RoleEntity(
	@Id
	@Column(name = "roleId") var roleId: String,
	@Column(name = "roleName") var roleName: String,
	@Column(name = "roleDesc") var roleDesc: String? = null,
	@Column(name = "createId") var createId: String? = null,
	@CreatedDate
	@Column(name = "createDate") var createDate: LocalDateTime,
	@Column(name = "updateId") var updateId: String? = null,
	@LastModifiedDate
	@Column(name = "updateDate") var updateDate: LocalDateTime,
	@ManyToMany
	@JoinTable(
		name = "awfUserRoleMap",
		joinColumns = arrayOf(JoinColumn(name = "roleId", referencedColumnName = "roleId")),
		inverseJoinColumns = arrayOf(JoinColumn(name = "userId", referencedColumnName = "userId"))
	)
	var userEntityList: List<UserEntity> = mutableListOf<UserEntity>(),
	@ManyToMany
	@JoinTable(
		name = "awfRoleAuthMap",
		joinColumns = arrayOf(JoinColumn(name = "roleId", referencedColumnName = "roleId")),
		inverseJoinColumns = arrayOf(JoinColumn(name = "authId", referencedColumnName = "authId"))
	)
	var authEntityList: List<AuthEntity> = mutableListOf<AuthEntity>()

) : Serializable