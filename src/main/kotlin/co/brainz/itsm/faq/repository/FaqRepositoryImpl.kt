/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.repository

import co.brainz.framework.util.AliceMessageSource
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.faq.constants.FaqConstants
import co.brainz.itsm.faq.dto.FaqSearchRequestDto
import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.faq.entity.QFaqEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class FaqRepositoryImpl(
    private val messageSource: AliceMessageSource
) : QuerydslRepositorySupport(FaqEntity::class.java), FaqRepositoryCustom {

    /**
     * FAQ 목록을 조회한다.
     */
    override fun findFaqs(searchRequestDto: FaqSearchRequestDto): QueryResults<FaqEntity> {
        val faq = QFaqEntity.faqEntity
        if (searchRequestDto.search?.isBlank() == false) {
            searchRequestDto.groupCodes =
                messageSource.getUserInputToCodes(FaqConstants.FAQ_CATEGORY_P_CODE, searchRequestDto.search!!)
        }

        val query = from(faq)
            .where(
                super.likeIgnoreCase(faq.faqTitle, searchRequestDto.search)
                    ?.or(super.inner(faq.faqGroup, searchRequestDto.groupCodes))
            ).orderBy(faq.faqGroup.asc())
            .limit(ItsmConstants.SEARCH_DATA_COUNT)
            .offset(searchRequestDto.offset)

        return query.fetchResults()
    }

    override fun findFaqTopList(limit: Long): List<FaqEntity> {
        val faq = QFaqEntity.faqEntity

        return from(faq).distinct()
            .orderBy(faq.createDt.desc())
            .limit(limit)
            .fetch()
    }
}
