/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.component.entity

import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.formRow.entity.WfFormRowEntity
import co.brainz.workflow.token.entity.WfTokenDataEntity
import java.io.Serializable
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "wf_component")
data class WfComponentEntity(

    @Id
    @Column(name = "component_id", length = 128)
    val componentId: String,

    @Column(name = "component_type", length = 100)
    val componentType: String,

    @Column(name = "mapping_id", length = 128)
    var mappingId: String,

    @Column(name = "is_topic")
    var isTopic: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    val form: WfFormEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_row_id")
    val formRow: WfFormRowEntity? = null

) : Serializable {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "properties", cascade = [CascadeType.REMOVE])
    val properties: MutableList<WfComponentPropertyEntity>? = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "component")
    val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
}
