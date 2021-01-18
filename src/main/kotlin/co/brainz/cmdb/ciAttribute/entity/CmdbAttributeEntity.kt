/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.entity

import co.brainz.framework.auth.entity.AliceUserEntity
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
@Table(name = "cmdb_attribute")
data class CmdbAttributeEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "attribute_id")
    var attributeId: String = "",

    @Column(name = "attribute_name", length = 100)
    var attributeName: String = "",

    @Column(name = "attribute_desc", length = 500)
    var attributeDesc: String? = null,

    @Column(name = "attribute_type", length = 100)
    var attributeType: String? = null,

    @Column(name = "attribute_text", length = 128)
    var attributeText: String = "",

    @Column(name = "attribute_value")
    var attributeValue: String? = "",

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_key", referencedColumnName = "user_key")
    var createUser: AliceUserEntity? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_user_key", referencedColumnName = "user_key")
    var updateUser: AliceUserEntity? = null
) : Serializable
