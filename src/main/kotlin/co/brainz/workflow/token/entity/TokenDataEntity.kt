package co.brainz.workflow.token.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.Table

@Entity
@Table(name = "wf_token_data")
@IdClass(TokenDataPk::class)
data class TokenDataEntity(

        @Id
        @JoinColumn(name = "inst_id")
        val instId: String,

        @Id
        @JoinColumn(name = "token_id")
        val tokenId: String,

        @Id
        @JoinColumn(name = "comp_id")
        val compId: String,

        @Column(name = "value")
        var value: String

) : Serializable

data class TokenDataPk(
        val instId: String = "",
        val tokenId: String = "",
        val compId: String = ""
) : Serializable
