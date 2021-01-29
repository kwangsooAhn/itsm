/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "cmdb_ci_history")
data class CIHistoryEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "history_id")
    var historyId: String = "",

    @Column(name = "ci_id", length = 128)
    var ciId: String = "",

    @Column(name = "seq")
    var seq: Int? = 0,

    @Column(name = "ci_no", length = 128)
    val ciNo: String? = null,

    @Column(name = "ci_name", length = 128)
    val ciName: String? = null,

    @Column(name = "ci_status", length = 100)
    val ciStatus: String? = null,

    @Column(name = "type_id", length = 128)
    val typeId: String? = null,

    @Column(name = "class_id", length = 128)
    val classId: String? = null,

    @Column(name = "ci_icon", length = 200)
    val ciIcon: String? = null,

    @Column(name = "ci_desc", length = 512)
    val ciDesc: String? = null,

    @Column(name = "automatic")
    val automatic: Boolean = false

) : Serializable
