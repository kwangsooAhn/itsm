package co.brainz.itsm.auth.entity

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Column
import java.time.LocalDateTime

@Entity
@Table(name = "awf_auth")
data class AuthEntity(
    @Id var authId: String,
    @Column(name = "authName") var authName: String? = null,
    @Column(name = "authDesc") var authDesc: String? = null,
    @Column(name = "createUserkey") var createUserkey: String? = null,
    @Column(name = "createDt") var createDt: LocalDateTime? = null,
    @Column(name = "updateUserkey") var updateUserkey: String? = null,
    @Column(name = "updateDt") var updateDt: LocalDateTime? = null
) : Serializable
