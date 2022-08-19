/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceDocumentRoleMapEntity
import co.brainz.framework.auth.entity.AliceDocumentRoleMapPk
import javax.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceDocumentRoleMapRepository : JpaRepository<AliceDocumentRoleMapEntity, AliceDocumentRoleMapPk>,
    AliceDocumentRoleMapRepositoryCustom {

    @Transactional
    fun deleteByDocumentId(documentId: String): Int
}
