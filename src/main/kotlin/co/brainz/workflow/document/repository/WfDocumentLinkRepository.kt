/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.repository

import co.brainz.workflow.document.entity.WfDocumentLinkEntity
import co.brainz.workflow.document.repository.querydsl.WfDocumentLinkRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfDocumentLinkRepository : JpaRepository<WfDocumentLinkEntity, String>, WfDocumentLinkRepositoryCustom {
    fun findByDocumentLinkId(documentLinkId: String): WfDocumentLinkEntity
    fun deleteByDocumentLinkId(documentLinkId: String): Int
}
