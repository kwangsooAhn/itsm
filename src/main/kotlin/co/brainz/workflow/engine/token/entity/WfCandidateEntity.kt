package co.brainz.workflow.engine.token.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "wf_candidate")
@IdClass(WfCandidatePk::class)
data class WfCandidateEntity(

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "token_id")
    val token: WfTokenEntity,

    @Id
    @Column(name = "candidate_type", length = 100, nullable = false)
    val candidateType: String,

    @Id
    @Column(name = "candidate_value", length = 512, nullable = false)
    val candidateValue: String

) : Serializable

data class WfCandidatePk(
    val token: String = "",
    val candidateType: String = "",
    val candidateValue: String = ""
) : Serializable
