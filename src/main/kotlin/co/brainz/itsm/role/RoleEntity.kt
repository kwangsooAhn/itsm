package co.brainz.itsm.role

import java.io.Serializable
import java.time.LocalDateTime
import org.springframework.format.annotation.DateTimeFormat
import co.brainz.itsm.user.UserEntity
import co.brainz.itsm.auth.AuthEntity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.Column
@Entity
@Table(name = "awf_role")
data public class RoleEntity(
    @Id
    @Column(name = "roleId") var roleId: String,
    @Column(name = "roleName") var roleName: String,
    @Column(name = "roleDesc") var roleDesc: String? = null,
    @Column(name = "createUserid") var createUserid: String? = null,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Column(name = "createDt") var createDt: LocalDateTime? = null,
    @Column(name = "updateUserid") var updateUserid: String? = null,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Column(name = "updateDt") var updateDt: LocalDateTime? = null,
    @ManyToMany
    @JoinTable(
        name = "awfRoleAuthMap",
        joinColumns = [JoinColumn(name = "roleId")],
        inverseJoinColumns = [JoinColumn(name = "authId")]
    )
    var authEntityList: List<AuthEntity>? = mutableListOf<AuthEntity>()

) : Serializable