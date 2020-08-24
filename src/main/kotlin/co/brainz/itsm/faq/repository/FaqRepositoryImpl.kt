/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.repository

import co.brainz.framework.util.AliceMessageSource
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.faq.constants.FaqConstants
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.dto.FaqSearchRequestDto
import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.faq.entity.QFaqEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class FaqRepositoryImpl(
    private val messageSource: AliceMessageSource
) : QuerydslRepositorySupport(FaqEntity::class.java), FaqRepositoryCustom {

    /**
     * FAQ 목록을 조회한다.
     */
    override fun findFaqs(searchRequestDto: FaqSearchRequestDto): List<FaqListDto> {
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
            .fetchResults()

        val faqList = mutableListOf<FaqListDto>()
        for (data in query.results) {
            val faqListDto = FaqListDto(
                faqId = data.faqId,
                faqGroup = data.faqGroup,
                faqTitle = data.faqTitle,
                faqContent = data.faqContent,
                totalCount = query.total,
                createDt = data.createDt,
                createUserName = data.createUser?.userName,
                updateDt = data.updateDt,
                updateUserName = data.updateUser?.userName
            )
            faqList.add(faqListDto)
        }

        return faqList.toList()
    }

    override fun findFaqTopList(limit: Long): List<FaqEntity> {
        val faq = QFaqEntity.faqEntity

        return from(faq).distinct()
            .orderBy(faq.createDt.desc())
            .limit(limit)
            .fetch()
    }
}
