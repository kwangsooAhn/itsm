package co.brainz.itsm.settings.auth

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column
import java.time.LocalDateTime
import javax.persistence.ManyToMany

@Entity
@Table(name = "awfAuth")
data public class AuthEntity(
    @Id var authId: String,
    @Column(name = "authName") var authName: String? = null,
    @Column(name = "authDesc") var authDesc: String? = null,
    @Column(name = "createId") var createId: String? = null,
    @Column(name = "createDate") var createDate: LocalDateTime? = null,
    @Column(name = "updateId") var updateId: String? = null,
    @Column(name = "updateDate") var updateDate: LocalDateTime? = null
) : Serializable 