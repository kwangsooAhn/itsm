package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "awf_url")
@IdClass(AliceUrlEntityPk::class)
data class AliceUrlEntity(
        @Id
        @Column(name = "url", length = 512)
        val url: String,

        @Id
        @Column(name = "method", length = 16)
        val method: String
): Serializable

data class AliceUrlEntityPk(
        val url: String = "",
        val method: String = ""
) : Serializable
