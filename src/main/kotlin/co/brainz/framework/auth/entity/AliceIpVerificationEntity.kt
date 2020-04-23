package co.brainz.framework.auth.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_ip_verification")
data class AliceIpVerificationEntity(
    @Id
    @Column(name = "ip_addr", length = 128)
    var ipAddr: String,

    @Column(name = "ip_explain", length = 512)
    var ipExplain: String? = null
) : Serializable, AliceMetaEntity()
