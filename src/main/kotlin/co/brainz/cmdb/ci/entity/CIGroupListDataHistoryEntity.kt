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
@Table(name = "cmdb_ci_group_list_data_history")
data class CIGroupListDataHistoryEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "data_history_id")
    val dataHistoryId: String = "",

    @Column(name = "seq")
    var seq: Int = 0,

    @Column(name = "ci_id", length = 128)
    var ciId: String = "",

    @Column(name = "attribute_id", length = 128)
    var attributeId: String = "",

    @Column(name = "c_attribute_id", length = 128)
    var cAttributeId: String = "",

    @Column(name = "c_attribute_seq")
    var cAttributeSeq: Int = 0,

    @Column(name = "c_attribute_name", length = 128)
    var cAttributeName: String? = null,

    @Column(name = "c_attribute_desc", length = 512)
    var cAttributeDesc: String? = null,

    @Column(name = "c_attribute_type", length = 100)
    var cAttributeType: String? = null,

    @Column(name = "c_attribute_text", length = 128)
    var cAttributeText: String? = null,

    @Column(name = "c_attribute_value")
    var cAttributeValue: String? = null,

    @Column(name = "c_value")
    var cValue: String? = null
) : Serializable
