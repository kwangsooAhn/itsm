package co.brainz.itsm.role

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.Column
import java.io.Serializable
import java.time.LocalDateTime
import co.brainz.itsm.user.UserEntity
import co.brainz.itsm.auth.AuthEntity
import com.fasterxml.jackson.annotation.JsonFormat

@Entity
@Table(name = "awf_role")
data public class RoleEntity(
    @Id
    @Column(name = "roleId") var roleId: String,
    @Column(name = "roleName") var roleName: String,
    @Column(name = "roleDesc") var roleDesc: String? = null,
    @Column(name = "createUserid") var createUserid: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "createDt") var createDt: LocalDateTime? = null,
    @Column(name = "updateUserid") var updateUserid: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "updateDt") var updateDt: LocalDateTime? = null,
    @JsonIgnore(access = JsonProperty.Access.READ_WRITE)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "awfRoleAuthMap",
        joinColumns = [JoinColumn(name = "roleId")],
        inverseJoinColumns = [JoinColumn(name = "authId")]
    )
    var authEntityList: List<AuthEntity>? = mutableListOf<AuthEntity>()

) : Serializable