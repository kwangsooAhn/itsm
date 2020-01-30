package co.brainz.workflow.process

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

/**
 * 프로세스 정보 엔티티
 * TODO wf_form_mst 관련 엔티티가 완성되면 OneToOne 추가해야한다.
 */
@Entity
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Table(name = "wf_proc_mst")
data class ProcessMstEntity(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "proc_id")
    val procId: String,

    @Column(name = "proc_key")
    val procKey: String,

    @Column(name = "proc_name")
    val procName: String,

    @Column(name = "proc_status")
    val procStatus: String,

    @Column(name = "proc_desc")
    val procDesc: String?,

    @Column(name = "create_userkey")
    val createUserkey: String?,

    @Column(name = "update_userkey")
    val updateUserkey: String?,

    @Column(name = "create_dt")
    val createDt: LocalDateTime?,

    @Column(name = "update_dt")
    val updateDt: LocalDateTime?

) : Serializable
