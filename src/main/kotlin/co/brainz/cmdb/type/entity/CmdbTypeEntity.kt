/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.type.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import java.io.Serializable
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import java.time.LocalDateTime
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_type_id")
    @NotFound(action = NotFoundAction.IGNORE)
    val ptypeId: CmdbTypeEntity? = null,

    @Column(name = "type_icon", length = 200)
    val typeIcon: String = "",

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
