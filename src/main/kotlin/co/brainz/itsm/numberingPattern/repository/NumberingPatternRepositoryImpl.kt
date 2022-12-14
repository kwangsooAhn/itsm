/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingPattern.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.numberingPattern.constants.NumberingPatternConstants
import co.brainz.itsm.numberingPattern.dto.NumberingPatternListDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternSearchCondition
import co.brainz.itsm.numberingPattern.entity.NumberingPatternEntity
import co.brainz.itsm.numberingPattern.entity.QNumberingPatternEntity
import co.brainz.itsm.numberingPattern.service.NumberingPatternService
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NumberingPatternRepositoryImpl(
    private val numberingPatternService: NumberingPatternService
) :
    QuerydslRepositorySupport(NumberingPatternEntity::class.java),
    NumberingPatternRepositoryCustom {

    override fun findPatternSearch(numberingPatternSearchCondition: NumberingPatternSearchCondition): PagingReturnDto {
        val pattern = QNumberingPatternEntity.numberingPatternEntity
        val query = from(pattern)
            .select(
                Projections.constructor(
                    NumberingPatternListDto::class.java,
                    pattern.patternId,
                    pattern.patternName,
                    pattern.patternType,
                    pattern.patternValue
                )
            )
            .where(builder(numberingPatternSearchCondition, pattern))
            .orderBy(pattern.patternName.asc(), pattern.patternType.asc(), pattern.patternValue.asc())
            .limit(numberingPatternSearchCondition.contentNumPerPage)
            .offset((numberingPatternSearchCondition.pageNum - 1) * numberingPatternSearchCondition.contentNumPerPage)
            .fetch()

        for (data in query) {
            data.patternValue = numberingPatternService.getPatternValue(data.patternType, data.patternValue)
            when (data.patternType) {
                NumberingPatternConstants.PatternType.TEXT.code -> {
                    data.patternType = NumberingPatternConstants.PatternType.SUMMARZIE_TEXT.code
                }
                NumberingPatternConstants.PatternType.DATE.code -> {
                    data.patternType = NumberingPatternConstants.PatternType.SUMMARZIE_DATE.code
                }
                NumberingPatternConstants.PatternType.SEQUENCE.code -> {
                    data.patternType = NumberingPatternConstants.PatternType.SUMMARZIE_SEQUENCE.code
                }
            }
        }

        val countQuery = from(pattern)
            .select(pattern.count())
            .where(builder(numberingPatternSearchCondition, pattern))
            .fetchOne()

        return PagingReturnDto(
            dataList = query,
            totalCount = countQuery
        )
    }

    private fun builder(
        numberingPatternSearchCondition: NumberingPatternSearchCondition,
        pattern: QNumberingPatternEntity
    ): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(super.likeIgnoreCase(pattern.patternName, numberingPatternSearchCondition.searchValue))
        return builder
    }
}
