/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.formRow.entity

import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.formGroup.entity.WfFormGroupEntity
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
@Table(name = "wf_form_row")
data class WfFormRowEntity(
    @Id
    @Column(name = "form_row_id", length = 128)
    var formRowId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_group_id")
    val formGroup: WfFormGroupEntity,

    @Column(name = "row_display_option")
    var rowDisplayOption: String = ""
) : Serializable {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "formRow", cascade = [CascadeType.REMOVE])
    val components: MutableList<WfComponentEntity> = mutableListOf()
}
