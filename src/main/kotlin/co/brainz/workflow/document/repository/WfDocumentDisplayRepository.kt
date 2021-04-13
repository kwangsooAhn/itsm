/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.repository

import co.brainz.workflow.document.entity.WfDocumentDisplayEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface WfDocumentDisplayRepository : JpaRepository<WfDocumentDisplayEntity, String> {

    @Transactional
    fun deleteByDocumentId(documentId: String): Int
    fun findByDocumentIdAndElementId(documentId: String, elementId: String): List<WfDocumentDisplayEntity>
}
