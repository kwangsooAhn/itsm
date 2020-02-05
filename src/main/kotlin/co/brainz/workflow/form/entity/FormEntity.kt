package co.brainz.workflow.form.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auditor.AliceMetaEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn

@Entity
@Table(name = "wf_form_mst")
data class FormEntity(
        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        @Column(name = "form_id") val formId: String,
        @Column(name = "form_name") var formName: String,
        @Column(name = "form_desc") var formDesc: String? = null,
        @Column(name = "form_status") var formStatus: String,

        @ManyToOne(targetEntity = AliceUserEntity::class, fetch = FetchType.LAZY)
        @JoinColumn(name = "create_userkey", insertable=false, updatable=false)
        var aliceUserEntity: AliceUserEntity?

): Serializable, AliceMetaEntity()