package co.brainz.workflow.instance.entity

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
@Table(name = "wf_inst_mst")
data class InstanceMstEntity(

        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        @Column(name = "instance_id", length = 128)
        val instanceId: String,

        @Column(name = "instance_status", length = 100)
        var instanceStatus: String,

        @Column(name = "instance_start_dt", nullable = false)
        val instanceStartDt: LocalDateTime? = null,

        @Column(name = "instance_end_dt", insertable = false)
        var instanceEndDt: LocalDateTime? = null,

        @JoinColumn(name = "process_id")
        val processId: String

) : Serializable
