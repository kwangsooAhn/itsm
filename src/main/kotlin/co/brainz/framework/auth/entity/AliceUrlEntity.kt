package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "awf_url")
data class AliceUrlEntity(
        @Id @Column(name = "url")
        val url: String,
        @Id @Column(name = "method")
        val method: String,

        @OneToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "awf_url_auth_map",
                joinColumns = [JoinColumn(name = "url"), JoinColumn(name = "method")],
                inverseJoinColumns = [JoinColumn(name = "auth_id")])
        val authEntities: List<AliceAuthEntity>
): Serializable