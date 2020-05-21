package co.brainz.workflow.engine.token.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "wf_candidate")
@IdClass(WfCandidatePk::class)
data class WfCandidateEntity(

    @Id
    @Column(name = "token_id", length = 128, nullable = false)
    val tokenId: String,

    @Id
    @Column(name = "", length = 100, nullable = false)
    val candidateType: String,

    @Id
    @Column(name = "", length = 512, nullable = false)
    val candidateValue: String

) : Serializable

data class WfCandidatePk(
    val tokenId: String = "",
    val candidateType: String = "",
    val candidateValue: String = ""
) : Serializable
