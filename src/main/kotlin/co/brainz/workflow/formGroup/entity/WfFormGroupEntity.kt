/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.formGroup.entity

import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.formRow.entity.WfFormRowEntity
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
@Table(name = "wf_form_group")
data class WfFormGroupEntity(
    @Id
    @Column(name = "form_group_id", length = 128)
    var formGroupId: String = "",

    @Column(name = "form_group_name", length = 256)
    var formGroupName: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    val form: WfFormEntity

) : Serializable {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "formGroup", cascade = [CascadeType.REMOVE])
    val formRows: MutableList<WfFormRowEntity> = mutableListOf()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "properties", cascade = [CascadeType.REMOVE])
    val properties: MutableList<WfFormGroupPropertyEntity> = mutableListOf()
}
