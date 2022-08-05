/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.itsm.role.dto.RoleListDto

interface AliceDocumentRoleMapRepositoryCustom {
    fun findRoleByDocumentId(documentId: String, type: String?): MutableList<RoleListDto>

    fun findDocumentIdsByRoles(roleIds: MutableList<String>): List<String>
}
