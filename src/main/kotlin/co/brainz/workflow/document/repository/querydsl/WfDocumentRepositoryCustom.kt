/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.repository.querydsl

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentListReturnDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto

interface WfDocumentRepositoryCustom : AliceRepositoryCustom {
    /**
     * 신청서 목록을 조회 후 리턴
     */
    fun findByDocuments(searchDto: RestTemplateDocumentSearchListDto): RestTemplateDocumentListReturnDto

    /**
     * 신청서 전체 리스트
     */
    fun findAllByDocuments(searchDto: RestTemplateDocumentSearchListDto): MutableList<RestTemplateDocumentDto>
}
