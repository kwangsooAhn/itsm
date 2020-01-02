package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_url")
data class AliceUrlEntity(
        @Id @Column(name = "url")
        val url: String,
        @Id @Column(name = "method")
        val method: String
): Serializable