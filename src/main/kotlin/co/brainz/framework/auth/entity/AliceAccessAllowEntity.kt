package co.brainz.framework.auth.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_access_allow")
data class AliceAccessAllowEntity(
        @Id
        @Column(name = "ip_addr", length = 128)
        var ipAddr: String,

        @Column(name = "ip_explain", length = 512)
        var ipExplain: String? = null,

        @Column(name = "create_dt")
        var createDt: LocalDateTime? = null,

        @Column(name = "create_user_key", length = 128)
        var createUserKey: String? = null,

        @Column(name = "update_dt")
        var updateDt: LocalDateTime? = null
): Serializable