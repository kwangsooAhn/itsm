/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.serviceCategory.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction

@Entity
@Table(name = "service_category")
data class ServiceCategoryEntity(
    @Id @Column(name = "service_code", length = 100)
    var serviceCode: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_service_code")
    @NotFound(action = NotFoundAction.IGNORE)
    var pServiceCode: ServiceCategoryEntity? = null,

    @Column(name = "service_name", length = 100)
    var serviceName: String = "",

    @Column(name = "service_desc")
    var serviceDesc: String? = null,

    @Column(name = "ava_goal")
    var avaGoal: String? = null,

    @Column(name = "start_date")
    var startDate: LocalDate? = null,

    @Column(name = "end_date")
    var endDate: LocalDate? = null,

    @Column(name = "editable")
    var editable: Boolean? = true,

    @Column(name = "use_yn")
    var useYn: Boolean? = true,

    @Column(name = "level")
    var level: Int? = null,

    @Column(name = "seq_num")
    var seqNum: Int? = null

) : Serializable, AliceMetaEntity()
