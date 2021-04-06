/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.entity

import co.brainz.workflow.instance.entity.WfInstanceEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "cmdb_ci_history")
data class CIHistoryEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "history_id")
    val historyId: String = "",

    @Column(name = "ci_id", length = 128)
    var ciId: String = "",

    @Column(name = "seq")
    var seq: Int? = 0,

    @Column(name = "ci_no", length = 128)
    var ciNo: String? = null,

    @Column(name = "ci_name", length = 128)
    var ciName: String? = null,

    @Column(name = "ci_status", length = 100)
    var ciStatus: String? = null,

    @Column(name = "type_id", length = 128)
    var typeId: String? = null,

    @Column(name = "class_id", length = 128)
    var classId: String? = null,

    @Column(name = "ci_icon", length = 200)
    var ciIcon: String? = null,

    @Column(name = "ci_desc", length = 512)
    var ciDesc: String? = null,

    @Column(name = "automatic")
    var automatic: Boolean? = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id")
    val instance: WfInstanceEntity? = null,

    @Column(name = "apply_dt")
    val applyDt: LocalDateTime? = null

) : Serializable
