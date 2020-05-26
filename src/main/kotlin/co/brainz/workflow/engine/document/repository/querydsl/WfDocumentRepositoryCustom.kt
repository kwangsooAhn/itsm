package co.brainz.workflow.engine.document.repository.querydsl

import co.brainz.framework.querydsl.AliceQueryDsl
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto

interface WfDocumentRepositoryCustom : AliceQueryDsl {

    /**
     * 신청서 목록을 조회 후 리턴
     */
    fun getDocumentsssss(searchDto: RestTemplateDocumentSearchListDto): List<WfDocumentEntity>
}
