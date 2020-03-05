package co.brainz.workflow.token.entity

import co.brainz.workflow.instance.dto.TicketDto
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.ColumnResult
import javax.persistence.ConstructorResult
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.NamedNativeQuery
import javax.persistence.SqlResultSetMapping
import javax.persistence.Table


@SqlResultSetMapping(name = "ticketMapping",
        classes = [
                ConstructorResult(
                        targetClass = TicketDto::class,
                        columns = [
                                ColumnResult(name = "tokenId", type = String::class),
                                ColumnResult(name = "ticketId", type = String::class),
                                ColumnResult(name = "ticketName", type = String::class),
                                ColumnResult(name = "ticketDesc", type = String::class),
                                ColumnResult(name = "createDt", type = LocalDateTime::class),
                                ColumnResult(name = "userKey", type = String::class)
                        ]
                )
        ]
)
@NamedNativeQuery(
        name = "findInstances",
        resultSetMapping = "ticketMapping",
        resultClass = TicketDto::class,
        query = "select wtm.token_id as tokenId\n" +
                "     , wim.instance_id as ticketId\n" +
                "     , wd.doc_name as ticketName\n" +
                "     , wd.doc_desc as ticketDesc\n" +
                "     , wim.instance_start_dt as createDt\n" +
                "     , wtm.assignee_id as userKey\n" +
                "from wf_doc wd \n" +
                "inner join wf_inst_mst wim on wd.proc_id = wim.process_id\n" +
                "inner join wf_token_mst wtm on wim.instance_id = wtm.instance_id\n" +
                "and wtm.token_status = :status\n" +
                "and assignee_id = :userKey"
)

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
