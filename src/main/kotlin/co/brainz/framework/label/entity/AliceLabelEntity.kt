/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.label.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "awf_label")
@IdClass(AliceLabelEntityPk::class)
data class AliceLabelEntity(
    @Id
    @Column(name = "label_target", length = 128)
    var labelTarget: String = "",

    @Id
    @Column(name = "label_target_id", length = 128)
    var labelTargetId: String = "",

    @Id
    @Column(name = "label_key", length = 128)
    var labelKey: String = "",

    @Column(name = "label_value", length = 512)
    var labelValue: String? = ""

) : Serializable, AliceMetaEntity()

data class AliceLabelEntityPk(
    var labelTarget: String = "",
    var labelTargetId: String = "",
    var labelKey: String = ""
) : Serializable
