/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.repository.querydsl

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.document.dto.DocumentDto
import co.brainz.itsm.document.dto.DocumentSearchCondition
import com.querydsl.core.QueryResults

interface WfDocumentLinkRepositoryCustom : AliceRepositoryCustom {
    /**
     * 신청서 목록을 조회 후 리턴
     */
    fun findByDocumentLink(documentSearchCondition: DocumentSearchCondition): QueryResults<DocumentDto>

    /**
     * 신청서 링크명 중복체크
     */
    fun existsByDocumentLinkName(documentName: String, documentLinkId: String): Boolean
}
