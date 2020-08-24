package co.brainz.workflow.document.repository.querydsl

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import com.querydsl.core.QueryResults

interface WfDocumentRepositoryCustom : AliceRepositoryCustom {

    /**
     * 신청서 목록을 조회 후 리턴
     */
    fun findByDocuments(searchDto: RestTemplateDocumentSearchListDto): QueryResults<WfDocumentEntity>
}
