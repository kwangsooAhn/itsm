/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciRelation.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "cmdb_ci_relation")
data class CIRelationEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "relation_id", length = 128)
    val relationId: String = "",

    @Column(name = "relation_type", length = 100)
    var relationType: String? = null,

    @Column(name = "ci_id")
    val ciId: String = "",

    @Column(name = "source_ci_id", length = 128)
    var sourceCIId: String = "",

    @Column(name = "target_ci_id", length = 128)
    var targetCIId: String = ""

) : Serializable
