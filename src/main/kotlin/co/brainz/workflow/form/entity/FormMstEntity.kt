package co.brainz.workflow.form.entity

import co.brainz.workflow.component.entity.ComponentMstEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "wf_form_mst")
data class FormMstEntity(
        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        @Column(name = "form_id")
        val formId: String,

        @Column(name = "form_name")
        var formName: String,

        @Column(name = "form_desc")
        var formDesc: String? = null,

        @Column(name = "form_status")
        var formStatus: String,

        @Column(name = "create_dt")
        var createDt: LocalDateTime? = null,

        @Column(name = "create_user_key")
        var createUserKey: String? = null,

        @Column(name = "update_dt")
        var updateDt: LocalDateTime? = null,

        @Column(name = "update_user_key")
        var updateUserKey: String? = null,

        @OneToMany(fetch = FetchType.LAZY)
        @JoinColumn(name = "form_id")
        val components: List<ComponentMstEntity>? = emptyList()

): Serializable
