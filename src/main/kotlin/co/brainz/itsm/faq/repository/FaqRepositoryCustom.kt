package co.brainz.itsm.faq.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.dto.FaqSearchRequestDto

interface FaqRepositoryCustom : AliceRepositoryCustom {

    /**
     * FAQ 목록을 조회한다.
     */
    fun findFaqs(searchRequestDto: FaqSearchRequestDto): List<FaqListDto>

    fun findFaqTopList(limit: Long): List<FaqEntity>
}
