/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.framework.util.AliceMessageSource
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.faq.constants.FaqConstants
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.dto.FaqSearchCondition
import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.faq.entity.QFaqEntity
import co.brainz.itsm.portal.dto.PortalTopDto
import com.querydsl.core.BooleanBuilder
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
    override fun findFaqs(faqSearchCondition: FaqSearchCondition): PagingReturnDto {
        val faq = QFaqEntity.faqEntity
        val user = QAliceUserEntity.aliceUserEntity
        val code = QCodeEntity.codeEntity
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
                    code.codeName.`as`("faqGroupName"),
                    faq.faqTitle,
                    faq.faqContent,
                    faq.createDt,
                    faq.createUser.userName
                )
            )
            .leftJoin(code).on(code.code.eq(faq.faqGroup))
            .innerJoin(faq.createUser, user)
            .where(builder(faqSearchCondition, faq))
            .orderBy(code.codeName.asc())

        if (faqSearchCondition.category?.isNotEmpty() == true) {
            query.where(faq.faqGroup.eq(faqSearchCondition.category))
        }
        if (faqSearchCondition.isPaging) {
            query.limit(faqSearchCondition.contentNumPerPage)
            query.offset((faqSearchCondition.pageNum - 1) * faqSearchCondition.contentNumPerPage)
        }

        val countQuery = from(faq)
            .select(faq.count())
            .leftJoin(code).on(code.code.eq(faq.faqGroup))
            .innerJoin(faq.createUser, user)
            .where(builder(faqSearchCondition, faq))

        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
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
        val code = QCodeEntity.codeEntity
        return from(faq)
            .select(
                Projections.constructor(
                    FaqListDto::class.java,
                    faq.faqId,
                    faq.faqGroup,
                    code.codeName.`as`("faqGroupName"),
                    faq.faqTitle,
                    faq.faqContent,
                    faq.createDt,
                    faq.createUser.userName
                )
            )
            .leftJoin(code).on(code.code.eq(faq.faqGroup))
            .innerJoin(faq.createUser, user)
            .where(faq.faqId.eq(faqId))
            .fetchOne()
    }

    private fun builder(faqSearchCondition: FaqSearchCondition, faq: QFaqEntity): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(
            super.likeIgnoreCase(faq.faqTitle, faqSearchCondition.searchValue)
        )
        return builder
    }
}
