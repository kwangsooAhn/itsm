/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.entity

import co.brainz.framework.auth.entity.AliceRoleEntity
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_organization_role_map")
@IdClass(OrganizationRoleMapEntityPk::class)
data class OrganizationRoleMapEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    var organization: OrganizationEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    var role: AliceRoleEntity
) : Serializable

data class OrganizationRoleMapEntityPk(
    val organization: String = "",
    val role: String = ""
) : Serializable
