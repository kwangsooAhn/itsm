/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.repository.querydsl

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.document.dto.DocumentDto
import co.brainz.itsm.document.dto.DocumentSearchCondition
import co.brainz.itsm.document.dto.FieldOptionDto
import co.brainz.workflow.document.entity.WfDocumentEntity
import com.querydsl.core.QueryResults

interface WfDocumentRepositoryCustom : AliceRepositoryCustom {
    /**
     * 신청서 목록을 조회 후 리턴
     */
    fun findByDocuments(documentSearchCondition: DocumentSearchCondition): QueryResults<DocumentDto>

    /**
     * 신청서 전체 리스트
     */
    fun findAllByDocuments(documentSearchCondition: DocumentSearchCondition): MutableList<DocumentDto>

    /**
     * 문서번호로 신청서 목록 조회
     */
    fun getDocumentListByNumberingId(numberingId: String): List<WfDocumentEntity>

    /**
     * 신청서 중복체크
     */
    fun existsByDocumentName(documentName: String, documentId: String): Boolean

    /**
     * 문서 조회
     */
    fun getDocumentListByIds(documentIds: Set<String>): List<WfDocumentEntity>

    /**
     * 이력 조회 컴포넌트 데이터 조회
     */
    fun getSearchFieldValues(fieldOptionDto: FieldOptionDto): List<Array<Any>>
}
