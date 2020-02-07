package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns
import javax.persistence.JoinTable
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "awf_url")
@IdClass(AliceUrlEntityPk::class)
data class AliceUrlEntity(
        @Id @Column(name = "url")
        val url: String,

        @Id @Column(name = "method")
        val method: String
): Serializable

data class AliceUrlEntityPk(
        val url: String = "",
        val method: String = ""
) : Serializable
