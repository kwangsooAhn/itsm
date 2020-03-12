package co.brainz.workflow.token.entity

import co.brainz.workflow.instance.entity.InstanceEntity
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
@IdClass(TokenDataPk::class)
data class TokenDataEntity(

        @Id
        @JoinColumn(name = "instance_id")
        val instanceId: String,

        @Id
        @JoinColumn(name = "token_id")
        val tokenId: String,

        @Id
        @JoinColumn(name = "component_id")
        val componentId: String,

        @Column(name = "value")
        var value: String

) : Serializable

data class TokenDataPk(
        val instanceId: String = "",
        val tokenId: String = "",
        val componentId: String = ""
) : Serializable
