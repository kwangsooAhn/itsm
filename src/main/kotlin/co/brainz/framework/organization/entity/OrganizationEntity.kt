/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.entity

import java.io.Serializable
import java.time.LocalDateTime
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
@Table(name = "awf_organization")
data class OrganizationEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "organization_id", length = 100)
    var organizationId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "p_organization_id")
    @NotFound(action = NotFoundAction.IGNORE)
    var pOrganization: OrganizationEntity? = null,

    @Column(name = "organization_name", length = 128)
    var organizationName: String? = null,

    @Column(name = "organization_desc", length = 128)
    var organizationDesc: String? = null,

    @Column(name = "use_yn")
    var useYn: Boolean? = true,

    @Column(name = "level")
    var level: Int? = null,

    @Column(name = "seq_num")
    var seqNum: Int? = null,

    @Column(name = "editable")
    var editable: Boolean? = true,

    @Column(name = "create_user_key", length = 128)
    var createUserKey: String? = null,

    @Column(name = "create_dt", nullable = false, updatable = false)
    var createDt: LocalDateTime? = null,

    @Column(name = "update_user_key", length = 128)
    var updateUserKey: String? = null,

    @Column(name = "update_dt", insertable = false)
    var updateDt: LocalDateTime? = null

) : Serializable {
    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    val organizationRoleMapEntities = mutableListOf<OrganizationRoleMapEntity>()
}
