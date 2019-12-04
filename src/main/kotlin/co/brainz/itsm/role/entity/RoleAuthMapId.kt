package co.brainz.itsm.role.entity

import javax.persistence.*
import java.io.Serializable
import java.util.Objects
import org.springframework.data.util.ProxyUtils

@Embeddable
public open class RoleAuthMapId(
	@Column(name = "roleId")
	private var roleId: String,
	@Column(name = "authId")
	private var authId: String

) : Serializable {

	public fun RoleAuthMapId() {}
	public fun RoleAuthMapId(roleId: String, authId: String) {
		this.roleId = roleId
		this.authId = authId
	}

	public fun getRoleId(): String {
		return roleId
	}

	public fun getAuthId(): String {
		return authId
	}

	public override fun equals(other: Any?): Boolean {
		other ?: return false
		if (this === other) return true
		if (javaClass != ProxyUtils.getUserClass(other)) return false
		other as RoleAuthMapId
		return Objects.equals(roleId, other.roleId) && Objects.equals(authId, other.authId)
	}

	public override fun hashCode(): Int {
		return Objects.hash(roleId, authId)
	}
}
