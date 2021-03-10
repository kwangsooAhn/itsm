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
@Table(name = "cmdb_ci_data_history")
data class CIDataHistoryEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "data_history_id")
    val dataHistoryId: String = "",

    @Column(name = "ci_id", length = 128)
    var ciId: String = "",

    @Column(name = "seq")
    var seq: Int = 0,

    @Column(name = "attribute_id", length = 128)
    var attributeId: String = "",

    @Column(name = "attribute_name", length = 128)
    var attributeName: String? = null,

    @Column(name = "attribute_desc", length = 512)
    var attributeDesc: String? = null,

    @Column(name = "attribute_type", length = 100)
    var attributeType: String? = null,

    @Column(name = "attribute_text", length = 128)
    var attributeText: String? = null,

    @Column(name = "attribute_value")
    var attributeValue: String? = null,

    @Column(name = "value")
    var value: String? = null
) : Serializable
