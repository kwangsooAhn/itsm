package co.brainz.workflow.engine.process.entity

import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.element.entity.WfElementEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

/**
 * 프로세스 정보 엔티티
 */
@Entity
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Table(name = "wf_process")
data class WfProcessEntity(
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

    @OneToMany(
        mappedBy = "processEntity",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE],
        orphanRemoval = true
    )
    var elementEntities: MutableList<WfElementEntity> = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "process")
    var document: MutableList<WfDocumentEntity> = mutableListOf()
}
