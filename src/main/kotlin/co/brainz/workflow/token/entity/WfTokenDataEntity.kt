package co.brainz.workflow.token.entity

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
@Table(name = "wf_token_data")
@IdClass(WfTokenDataPk::class)
data class WfTokenDataEntity(
    @Id
    @Column(name = "token_id")
    val tokenId: String,

    @Id
    @Column(name = "component_id")
    val componentId: String,

    @Column(name = "value")
    var value: String

) : Serializable

data class WfTokenDataPk(
    val tokenId: String = "",
    val componentId: String = ""
) : Serializable
