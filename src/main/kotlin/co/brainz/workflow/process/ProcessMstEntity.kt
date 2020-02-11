package co.brainz.workflow.process

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.workflow.form.entity.FormEntity
import co.brainz.workflow.utility.WFMetaEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
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
    val formEntity: FormEntity?,

    @Column(name = "proc_key")
    val procKey: String,

    @Column(name = "proc_name")
    val procName: String,

    @Column(name = "proc_status")
    val procStatus: String,

    @Column(name = "proc_desc")
    val procDesc: String?,

    @ManyToOne(targetEntity=AliceUserEntity::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_key", insertable=false, updatable=false)
    var aliceUserEntity: AliceUserEntity? = null

) : Serializable, WFMetaEntity()
