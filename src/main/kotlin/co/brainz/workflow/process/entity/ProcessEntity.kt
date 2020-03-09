package co.brainz.workflow.process.entity

import co.brainz.workflow.document.entity.DocumentEntity
import co.brainz.workflow.element.entity.ElementEntity
import co.brainz.workflow.form.entity.FormEntity
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
@Table(name = "wf_process")
data class ProcessEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @Column(name = "process_id", length = 128)
    var processId: String = "",

    @Column(name = "process_name", length = 256)
    var processName: String = "",

    @Column(name = "process_status", length = 100)
    var processStatus: String = "",

    @Column(name = "process_desc", length = 256)
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
    var formEntity: FormEntity? = null

    @OneToMany(
        mappedBy = "processEntity",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE],
        orphanRemoval = true
    )
    var elementEntities: MutableList<ElementEntity> = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "processes")
    var processes: MutableList<DocumentEntity> = mutableListOf()
}
