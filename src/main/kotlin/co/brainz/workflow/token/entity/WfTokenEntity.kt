package co.brainz.workflow.token.entity

import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "wf_token")
data class WfTokenEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "token_id", length = 128)
    val tokenId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id")
    val element: WfElementEntity,

    @Column(name = "token_status", length = 100)
    var tokenStatus: String,

    @Column(name = "token_start_dt", nullable = false)
    val tokenStartDt: LocalDateTime? = null,

    @Column(name = "token_end_dt", nullable = false)
    var tokenEndDt: LocalDateTime? = null,

    @Column(name = "assignee_id", length = 128)
    var assigneeId: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id")
    val instance: WfInstanceEntity

) : Serializable {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "token")
    var tokenDataEntities: List<WfTokenDataEntity> = mutableListOf()
}
