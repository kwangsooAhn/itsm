package co.brainz.workflow.token.entity

import co.brainz.workflow.instance.entity.InstanceMstEntity
import co.brainz.workflow.process.entity.ProcessMstEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.Table

@Entity
@Table(name = "wf_token_mst")
data class TokenMstEntity(

        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        @Column(name = "token_id", length = 128)
        val tokenId: String,

        @JoinColumn(name = "instance_id")
        val instanceId: String,

        @JoinColumn(name = "element_id")
        val elementId: String,

        @Column(name = "token_status", length = 100)
        var tokenStatus: String,

        @Column(name = "token_start_dt", nullable = false)
        val tokenStartDt: LocalDateTime? = null,

        @Column(name = "token_end_dt", nullable = false)
        var tokenEndDt: LocalDateTime? = null,

        @Column(name = "", length = 128)
        var assigneeId: String? = null,

        @Column(name = "", length = 256)
        var assigneeType: String? = null

) : Serializable
