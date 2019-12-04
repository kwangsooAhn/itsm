package co.brainz.itsm.user.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*
import co.brainz.itsm.role.entity.RoleEntity

@Entity
@Table(name = "awfUser")
data class UserEntity(
	@Id @Column(name = "userId") val userId: String? = null,
	@Column(name = "userName") val userName: String? = null,
	@Column(name = "password") val password: String? = null,
	@Column(name = "email") val email: String? = null,
	@Column(name = "useYn") val useYn: Boolean? = null,
	@Column(name = "tryLoginCount") val tryLoginCount: Int? = null,
	@Column(name = "createId") val createId: String? = null,
	@Column(name = "updateId") val updateId: String? = null
) : Serializable {}