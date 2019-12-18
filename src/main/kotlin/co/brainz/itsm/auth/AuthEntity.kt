package co.brainz.itsm.auth

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column
import java.time.LocalDateTime
import javax.persistence.ManyToMany

@Entity
@Table(name = "awf_auth")
data public class AuthEntity(
    @Id var authId: String,
    @Column(name = "authName") var authName: String? = null,
    @Column(name = "authDesc") var authDesc: String? = null,
    @Column(name = "createUserid") var createUserid: String? = null,
    @Column(name = "createDt") var createDt: LocalDateTime? = null,
    @Column(name = "updateUserid") var updateUserid: String? = null,
    @Column(name = "updateDt") var updateDt: LocalDateTime? = null
) : Serializable 