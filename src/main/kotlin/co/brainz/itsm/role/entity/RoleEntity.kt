package co.brainz.itsm.role.entity

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GenerationType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column

@Entity
@Table(name = "awfRole")
public data class RoleEntity(
	@Id
	@Column(name = "roleNo")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var roleNo: Int,
	@Column(name = "roleName")
	var roleName: String,
	@Column(name = "roleDesc") var roleDesc: String? = null,
	@Column(name = "createId") var createId: String? = null,
	@Column(name = "createDate") var createDate: String? = null,
	@Column(name = "updateId") var updateId: String? = null,
	@Column(name = "updateDate") var updateDate: String? = null
) : Serializable {

}