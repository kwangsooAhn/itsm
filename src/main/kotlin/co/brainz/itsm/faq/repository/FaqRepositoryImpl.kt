package co.brainz.itsm.faq.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.util.AliceMessageSource
import co.brainz.itsm.faq.constants.FaqConstants
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.dto.FaqSearchRequestDto
import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.faq.entity.QFaqEntity
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
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
        val user = QAliceUserEntity.aliceUserEntity

        if (searchRequestDto.search?.isBlank() == false) {
            searchRequestDto.groupCodes =
                messageSource.getUserInputToCodes(FaqConstants.FAQ_CATEGORY_P_CODE, searchRequestDto.search!!)
        }

        val query = from(faq)
            .where(
                super.likeIgnoreCase(faq.faqTitle, searchRequestDto.search)
                    ?.or(super.inner(faq.faqGroup, searchRequestDto.groupCodes))
            ).orderBy(faq.faqGroup.asc())

        return query.select(
            Projections.constructor(
                FaqListDto::class.java,
                faq.faqId,
                faq.faqGroup,
                faq.faqTitle,
                faq.faqContent,
                faq.createDt,
                JPAExpressions.select(user.userName).from(user).where(user.eq(faq.createUser)),
                faq.updateDt,
                JPAExpressions.select(user.userName).from(user).where(user.eq(faq.updateUser))
            )
        ).fetch()
    }

    override fun findFaqTopList(limit: Long): List<FaqEntity> {
        val faq = QFaqEntity.faqEntity

        return from(faq).distinct()
            .orderBy(faq.createDt.desc())
            .limit(limit)
            .fetch()
    }
}
