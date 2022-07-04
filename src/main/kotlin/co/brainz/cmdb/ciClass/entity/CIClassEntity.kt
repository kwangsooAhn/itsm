/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
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
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction

@Entity
@Table(name = "cmdb_class")
data class CIClassEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "class_id")
    var classId: String = "",

    @Column(name = "class_name", length = 128)
    var className: String = "",

    @Column(name = "class_desc", length = 512)
    var classDesc: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_class_id")
    @NotFound(action = NotFoundAction.IGNORE)
    var pClass: CIClassEntity? = null,

    @Column(name = "class_level")
    var classLevel: Int? = null,

    @Column(name = "class_seq")
    var classSeq: Int? = null
) : Serializable, AliceMetaEntity() {
    @OneToMany(mappedBy = "ciClass", fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    val ciClassAttributeMapEntities = mutableListOf<CIClassAttributeMapEntity>()
}
