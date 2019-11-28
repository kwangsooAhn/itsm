package co.brainz.itsm.role.entity

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column
import java.time.LocalDateTime

@Entity
@Table(name = "awfAuth")
data public class AuthEntity(
	@Id var authId: String,
	@Column(name = "authName") var authName: String,
	@Column(name = "authDesc") var authDesc: String,
	@Column(name = "createId") var createId: String,
	@Column(name = "createDate") var createDate: LocalDateTime,
	@Column(name = "updateId") var updateId: String,
	@Column(name = "updateDate") var updateDate: LocalDateTime
) : Serializable 