/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.repository.querydsl

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.workflow.provider.dto.RestTemplateDocumentListDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto

interface WfDocumentRepositoryCustom : AliceRepositoryCustom {

    /**
     * 신청서 목록을 조회 후 리턴
     */
    fun findByDocuments(searchDto: RestTemplateDocumentSearchListDto): MutableList<RestTemplateDocumentListDto>
}
