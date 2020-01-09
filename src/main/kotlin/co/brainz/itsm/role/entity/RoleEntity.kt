package co.brainz.itsm.role.entity

import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.Column
import java.io.Serializable
import java.time.LocalDateTime
import co.brainz.itsm.auth.entity.AuthEntity
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.FetchType

@Entity
@Table(name = "awf_role")
data public class RoleEntity(
    @Id
    @Column(name = "roleId") var roleId: String,
    @Column(name = "roleName") var roleName: String,
    @Column(name = "roleDesc") var roleDesc: String? = null,
    @Column(name = "createUserkey") var createUserkey: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "createDt") var createDt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updateUserkey") var updateUserkey: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "updateDt") var updateDt: LocalDateTime? = null,
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "awfRoleAuthMap",
        joinColumns = [JoinColumn(name = "roleId")],
        inverseJoinColumns = [JoinColumn(name = "authId")]
    )
    var authEntityList: List<AuthEntity>?
) : Serializable
