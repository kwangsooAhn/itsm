package co.brainz.workflow.token.entity

import co.brainz.workflow.component.entity.WfComponentEntity
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "token_id")
    val token: WfTokenEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    val component: WfComponentEntity,

    @Column(name = "value")
    var value: String

) : Serializable

data class WfTokenDataPk(
    val token: String = "",
    val component: String = ""
) : Serializable
