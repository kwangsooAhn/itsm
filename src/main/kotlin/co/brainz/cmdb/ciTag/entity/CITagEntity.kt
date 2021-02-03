/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciTag.entity

import co.brainz.cmdb.ci.entity.CIEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "cmdb_ci_tag")
@IdClass(CITagPkey::class)
data class CITagEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ci_id", insertable = false, updatable = false)
    val ci: CIEntity,

    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "tag_id", length = 128)
    val tagId: String = "",

    @Column(name = "tag_name", length = 128)
    var tagName: String? = null

) : Serializable

data class CITagPkey(
    val ci: String = "",
    val tagId: String = ""
) : Serializable
