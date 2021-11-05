/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.repository.querydsl

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.provider.dto.DocumentSearchCondition
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import com.querydsl.core.QueryResults

interface WfDocumentRepositoryCustom : AliceRepositoryCustom {
    /**
     * 신청서 목록을 조회 후 리턴
     */
    fun findByDocuments(documentSearchCondition: DocumentSearchCondition): QueryResults<WfDocumentEntity>

    /**
     * 신청서 전체 리스트
     */
    fun findAllByDocuments(documentSearchCondition: DocumentSearchCondition): MutableList<RestTemplateDocumentDto>

    /**
     * 문서번호로 신청서 목록 조회
     */
    fun getDocumentListByNumberingId(numberingId: String): List<WfDocumentEntity>

    /**
     * 신청서 중복체크
     */
    fun findDuplicationDocumentName(documentName: String, documentId: String): Long
}
