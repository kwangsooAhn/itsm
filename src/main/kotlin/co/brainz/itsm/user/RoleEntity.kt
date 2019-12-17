package co.brainz.itsm.user

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awfRole")
data class RoleEntity(
        @Id var roleId: String,
        @Column(name = "roleName") var roleName: String,
        @Column(name = "roleDesc") var roleDesc: String?,
        @Column(name = "createUserid") var createUserid: String?,
        @Column(name = "createDt") var createDt: LocalDateTime?,
        @Column(name = "updateUserid") var updateUserid: String?,
        @Column(name = "updateDt") var updateDt: LocalDateTime?

) : Serializable