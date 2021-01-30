/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.entity

import co.brainz.cmdb.ciClass.entity.CIClassEntity
import co.brainz.cmdb.ciType.entity.CITypeEntity
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
@Table(name = "cmdb_ci")
data class CIEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "ci_id")
    val ciId: String = "",

    @Column(name = "ci_no", length = 128)
    var ciNo: String? = "",

    @Column(name = "ci_name", length = 128)
    var ciName: String? = "",

    @Column(name = "ci_status", length = 100)
    var ciStatus: String? = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    val ciTypeEntity: CITypeEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    val ciClassEntity: CIClassEntity,

    @Column(name = "ci_icon", length = 200)
    var ciIcon: String? = null,

    @Column(name = "ci_desc", length = 512)
    var ciDesc: String? = null,

    @Column(name = "automatic")
    var automatic: Boolean = false,

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

    @OneToMany(
        mappedBy = "ci",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE],
        orphanRemoval = true
    )
    val ciTagEntity: MutableList<CITagEntity> = mutableListOf()

    @OneToMany(
        mappedBy = "ci",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE],
        orphanRemoval = true
    )
    val ciDataEntities = mutableListOf<CIDataEntity>()
}
