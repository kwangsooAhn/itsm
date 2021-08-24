/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingPattern.repository

import co.brainz.itsm.numberingPattern.constants.NumberingPatternConstants
import co.brainz.itsm.numberingPattern.dto.NumberingPatternListDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternSearchCondition
import co.brainz.itsm.numberingPattern.entity.NumberingPatternEntity
import co.brainz.itsm.numberingPattern.entity.QNumberingPatternEntity
import co.brainz.itsm.numberingPattern.service.NumberingPatternService
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NumberingPatternRepositoryImpl(
    private val numberingPatternService: NumberingPatternService
) :
    QuerydslRepositorySupport(NumberingPatternEntity::class.java),
    NumberingPatternRepositoryCustom {

    override fun findPatternSearch(numberingPatternSearchCondition: NumberingPatternSearchCondition): QueryResults<NumberingPatternListDto> {
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
            .where(
                super.like(pattern.patternName, numberingPatternSearchCondition.searchValue)
            )
            .orderBy(pattern.patternName.desc())
            .fetchResults()

        for (data in query.results) {
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
        return query
    }
}
