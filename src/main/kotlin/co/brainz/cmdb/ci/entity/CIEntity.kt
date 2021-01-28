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
@Table(name = "cmdb_ci")
data class CIEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ci_id")
    var ciId: String = "",

    @Column(name = "ci_no", length = 128)
    val ciNo: String? = "",

    @Column(name = "ci_name", length = 128)
    val ciName: String? = "",

    @Column(name = "type_id", length = 128)
    val typeId: String? = "",

    @Column(name = "class_id", length = 128)
    val classId: String? = "",

    @Column(name = "ci_icon", length = 200)
    val ciIcon: String? = "",

    @Column(name = "ci_desc", length = 500)
    val ciDesc: String? = "",

    @Column(name = "automatic")
    val automatic: Boolean = false

) : Serializable
