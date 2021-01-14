/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "cmdb_attribute")
data class CmdbAttributeEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "attribute_id")
    var attributeId: String = "",

    @Column(name = "attribute_name", length = 100)
    val attributeName: String = "",

    @Column(name = "attribute_desc", length = 500)
    val attributeDesc: String? = null,

    @Column(name = "attribute_type", length = 100)
    val attributeType: String? = null,

    @Column(name = "attribute_text", length = 128)
    val attributeText: String = "",

    @Column(name = "attribute_value")
    val attributeValue: String = ""
) : Serializable, AliceMetaEntity()
