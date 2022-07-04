/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciType.entity

import co.brainz.cmdb.ciClass.entity.CIClassEntity
import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction

@Entity
@Table(name = "cmdb_type")
data class CITypeEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "type_id")
    var typeId: String = "",

    @Column(name = "type_name", length = 128)
    val typeName: String? = "",

    @Column(name = "type_desc", length = 512)
    val typeDesc: String? = null,

    @Column(name = "type_alias", length = 128)
    val typeAlias: String? = "",

    @Column(name = "type_level")
    var typeLevel: Int? = null,

    @Column(name = "type_seq")
    var typeSeq: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_type_id")
    @NotFound(action = NotFoundAction.IGNORE)
    val pType: CITypeEntity? = null,

    @Column(name = "type_icon", length = 200)
    var typeIcon: String? = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", referencedColumnName = "class_id")
    var ciClass: CIClassEntity
) : Serializable, AliceMetaEntity()
