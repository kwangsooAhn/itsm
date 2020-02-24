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

        @JoinColumn(name = "inst_id")
        val instId: String,

        @JoinColumn(name = "elem_id")
        val elemId: String,

        @Column(name = "token_status", length = 100)
        var tokenStatus: String,

        @Column(name = "token_start_dt", nullable = false)
        val tokenStartDt: LocalDateTime? = null,

        @Column(name = "token_end_dt", nullable = false)
        var tokenEndDt: LocalDateTime? = null,

        @Column(name = "", length = 128)
        val assigneeId: String? = null,

        @Column(name = "", length = 256)
        val assigneeType: String? = null

) : Serializable
