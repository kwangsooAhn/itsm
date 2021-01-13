/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.`class`.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "cmdb_class")
data class CmdbClassEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "class_id")
    var classId: String = "",

    @Column(name = "class_name", length = 100)
    var className: String = "",

    @Column(name = "class_desc", length = 500)
    var classDesc: String? = null,

    @Column(name = "p_class_id")
    var pClassId: String? = null,

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
) : Serializable {
    @OneToMany(mappedBy = "cmdbClass", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    val cmdbClassAttributeMapEntities = mutableListOf<CmdbClassAttributeMapEntity>()
}
