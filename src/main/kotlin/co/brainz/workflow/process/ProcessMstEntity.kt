package co.brainz.workflow.process

import co.brainz.workflow.form.entity.FormMstEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

/**
 * 프로세스 정보 엔티티
 */
@Entity
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Table(name = "wf_proc_mst")
data class ProcessMstEntity(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "proc_id")
    val procId: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    val formMstEntity: FormMstEntity?,

    @Column(name = "proc_key")
    val procKey: String,

    @Column(name = "proc_name")
    val procName: String,

    @Column(name = "proc_status")
    val procStatus: String,

    @Column(name = "proc_desc")
    val procDesc: String?,

    @Column(name = "create_dt")
    var createDt: LocalDateTime? = null,

    @Column(name = "create_userkey")
    var createUserkey: String? = null,

    @Column(name = "update_dt")
    var updateDt: LocalDateTime? = null,

    @Column(name = "update_userkey")
    var updateUserkey: String? = null

) : Serializable
