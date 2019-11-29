package co.brainz.itsm.settings.user

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
        @Column(name = "createId") var createId: String?,
        @Column(name = "createDate") var createDate: LocalDateTime?,
        @Column(name = "updateId") var updateId: String?,
        @Column(name = "updateDate") var updateDate: LocalDateTime?

) : Serializable