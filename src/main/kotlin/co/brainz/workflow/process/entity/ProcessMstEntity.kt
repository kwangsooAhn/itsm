package co.brainz.workflow.process.entity

import co.brainz.workflow.document.entity.DocumentEntity
import co.brainz.workflow.element.entity.ElementMstEntity
import co.brainz.workflow.form.entity.FormMstEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

/**
 * 프로세스 정보 엔티티
 */
@Entity
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Table(name = "wf_proc_mst")
data class ProcessMstEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @Column(name = "proc_id", length = 128)
    var processId: String = "",

    @Column(name = "proc_key", length = 256)
    var processKey: String = "",

    @Column(name = "proc_name", length = 256)
    var processName: String = "",

    @Column(name = "proc_status", length = 100)
    var processStatus: String = "",

    @Column(name = "proc_desc", length = 256)
    var processDesc: String? = null,

    @Column(name = "create_dt")
    var createDt: LocalDateTime? = null,

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String? = null,

    @Column(name = "update_dt")
    var updateDt: LocalDateTime? = null,

    @Column(name = "update_user_key", length = 128)
    var updateUserKey: String? = null

) : Serializable {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    var formMstEntity: FormMstEntity = FormMstEntity()

    @OneToMany(
        mappedBy = "processMstEntity",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE],
        orphanRemoval = true
    )
    var elementMstEntities: MutableList<ElementMstEntity> = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "processes")
    var processes: MutableList<DocumentEntity> = mutableListOf()
}
