/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.type.entity

import java.io.Serializable
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "cmdb_type")
data class CmdbTypeEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "type_id")
    var typeId: String = "",

    @Column(name = "type_name", length = 100)
    val typeName: String? = null,

    @Column(name = "type_desc", length = 500)
    val typeDesc: String? = null,

    @Column(name = "type_level")
    var typeLevel: Int? = null,

    @Column(name = "default_class_id", length = 128)
    val defaultClassId: String = "",

    @Column(name = "p_type_id")
    val ptypeId: String = "",

    @Column(name = "type_icon", length = 200)
    val typeIcon: String = ""
) : Serializable
