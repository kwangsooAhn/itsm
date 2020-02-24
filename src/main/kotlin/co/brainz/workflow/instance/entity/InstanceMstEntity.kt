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
        @Column(name = "inst_id", length = 128)
        val instId: String,

        @Column(name = "inst_status", length = 100)
        var instStatus: String,

        @Column(name = "inst_start_dt", nullable = false)
        val instStartDt: LocalDateTime? = null,

        @Column(name = "inst_end_dt", insertable = false)
        var instEndDt: LocalDateTime? = null,

        @JoinColumn(name = "proc_id")
        val procId: String

) : Serializable
