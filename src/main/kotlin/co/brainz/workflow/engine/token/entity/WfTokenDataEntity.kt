package co.brainz.workflow.engine.token.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.Table

@Entity
@Table(name = "wf_token_data")
@IdClass(WfTokenDataPk::class)
data class WfTokenDataEntity(
    @Id
    @JoinColumn(name = "token_id")
    val tokenId: String,

    @Id
    @JoinColumn(name = "component_id")
    val componentId: String,

    @Column(name = "value")
    var value: String

) : Serializable

data class WfTokenDataPk(
    val tokenId: String = "",
    val componentId: String = ""
) : Serializable
