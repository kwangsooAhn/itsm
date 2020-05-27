package co.brainz.workflow.engine.document.repository.querydsl

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import org.springframework.data.domain.Page

interface WfDocumentRepositoryCustom : AliceRepositoryCustom {

    /**
     * 신청서 목록을 조회 후 리턴
     */
    fun findByDocuments(searchDto: RestTemplateDocumentSearchListDto): List<RestTemplateDocumentDto>
}
