package co.brainz.workflow.form.entity

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
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

        @Column(name = "create_userkey")
        var createUserkey: String? = null,

        @Column(name = "update_dt")
        var updateDt: LocalDateTime? = null,

        @Column(name = "update_userkey")
        var updateUserkey: String? = null

): Serializable
