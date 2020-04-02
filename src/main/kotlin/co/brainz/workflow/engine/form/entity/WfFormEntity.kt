package co.brainz.workflow.engine.form.entity

import co.brainz.workflow.engine.component.entity.WfComponentEntity
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
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

@Entity
@Table(name = "wf_form")
data class WfFormEntity(

        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        @Column(name = "form_id", length = 128)
        var formId: String = "",

        @Column(name = "form_name", length = 256)
        var formName: String = "",

        @Column(name = "form_desc", length = 256)
        var formDesc: String? = null,

        @Column(name = "form_status", length = 100)
        var formStatus: String? = null,

        @Column(name = "create_dt", nullable = false, updatable = false)
        var createDt: LocalDateTime? = null,

        @Column(name = "create_user_key", length = 128)
        var createUserKey: String? = null,

        @Column(name = "update_dt", insertable = false)
        var updateDt: LocalDateTime? = null,

        @Column(name = "update_user_key", length = 128)
        var updateUserKey: String? = null

): Serializable {
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "form", cascade = [CascadeType.REMOVE])
        val components: MutableList<WfComponentEntity>? = mutableListOf()

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "form")
        val document: MutableList<WfDocumentEntity>? = mutableListOf()
}
