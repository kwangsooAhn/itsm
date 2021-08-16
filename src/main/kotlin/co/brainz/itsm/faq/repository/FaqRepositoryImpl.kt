/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.faq.constants.FaqConstants
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.dto.FaqListReturnDto
import co.brainz.itsm.faq.dto.FaqSearchCondition
import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.faq.entity.QFaqEntity
import co.brainz.itsm.portal.dto.PortalTopDto
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class FaqRepositoryImpl(
    private val messageSource: AliceMessageSource
) : QuerydslRepositorySupport(FaqEntity::class.java), FaqRepositoryCustom {

    /**
     * FAQ 목록을 조회한다.
     */
    override fun findFaqs(faqSearchCondition: FaqSearchCondition): FaqListReturnDto {
        val faq = QFaqEntity.faqEntity
        val user = QAliceUserEntity.aliceUserEntity
        if (faqSearchCondition.searchValue?.isNotBlank() == true) {
            faqSearchCondition.groupCodes =
                messageSource.getUserInputToCodes(FaqConstants.FAQ_CATEGORY_P_CODE, faqSearchCondition.searchValue)
        }

        val query = from(faq)
            .select(
                Projections.constructor(
                    FaqListDto::class.java,
                    faq.faqId,
                    faq.faqGroup,
                    faq.faqTitle,
                    faq.faqContent,
                    faq.createDt,
                    faq.createUser.userName
                )
            )
            .innerJoin(faq.createUser, user)
            .where(
                super.like(faq.faqTitle, faqSearchCondition.searchValue)
                    ?.or(super.inner(faq.faqGroup, faqSearchCondition.groupCodes))
            ).orderBy(faq.faqGroup.asc())
            .limit(faqSearchCondition.contentNumPerPage)
            .offset((faqSearchCondition.pageNum - 1) * faqSearchCondition.contentNumPerPage)
            .fetchResults()

        return FaqListReturnDto(
            data = query.results,
            paging = AlicePagingData(
                totalCount = query.total,
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    override fun findFaqTopList(limit: Long): List<PortalTopDto> {
        val faq = QFaqEntity.faqEntity

        return from(faq).distinct()
            .select(
                Projections.constructor(
                    PortalTopDto::class.java,
                    faq.faqId,
                    faq.faqTitle,
                    faq.faqContent,
                    faq.createDt
                )
            )
            .orderBy(faq.createDt.desc())
            .limit(limit)
            .fetch()
    }

    override fun findFaq(faqId: String): FaqListDto {
        val faq = QFaqEntity.faqEntity
        val user = QAliceUserEntity.aliceUserEntity
        return from(faq)
            .select(
                Projections.constructor(
                    FaqListDto::class.java,
                    faq.faqId,
                    faq.faqGroup,
                    faq.faqTitle,
                    faq.faqContent,
                    faq.createDt,
                    faq.createUser.userName
                )
            )
            .innerJoin(faq.createUser, user)
            .where(faq.faqId.eq(faqId))
            .fetchOne()
    }
}
