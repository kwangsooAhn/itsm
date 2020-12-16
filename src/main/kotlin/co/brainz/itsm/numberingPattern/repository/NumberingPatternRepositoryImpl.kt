package co.brainz.itsm.numberingPattern.repository

import co.brainz.itsm.numberingPattern.constants.NumberingPatternConstants
import co.brainz.itsm.numberingPattern.dto.NumberingPatternListDto
import co.brainz.itsm.numberingPattern.entity.NumberingPatternEntity
import co.brainz.itsm.numberingPattern.entity.QNumberingPatternEntity
import co.brainz.itsm.numberingPattern.service.NumberingPatternService
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NumberingPatternRepositoryImpl(
    private val numberingPatternService: NumberingPatternService
) : QuerydslRepositorySupport(NumberingPatternEntity::class.java), NumberingPatternRepositoryCustom {

    override fun findPatternSearch(search: String): MutableList<NumberingPatternListDto> {
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
                super.likeIgnoreCase(pattern.patternName, search)
            )
            .orderBy(pattern.patternName.desc())
            .fetchResults()

        val numberingPatternList = mutableListOf<NumberingPatternListDto>()
        var patternType = ""
        for (data in query.results) {

            when (data.patternType) {
                NumberingPatternConstants.PatternType.TEXT.code -> {
                    patternType = NumberingPatternConstants.PatternType.SUMMARZIE_TEXT.code
                }
                NumberingPatternConstants.PatternType.DATE.code -> {
                    patternType = NumberingPatternConstants.PatternType.SUMMARZIE_DATE.code
                }
                NumberingPatternConstants.PatternType.SEQUENCE.code -> {
                    patternType = NumberingPatternConstants.PatternType.SUMMARZIE_SEQUENCE.code
                }
            }

            val numberingPatternListDto = NumberingPatternListDto(
                patternId = data.patternId,
                patternName = data.patternName,
                patternType = patternType,
                patternValue = numberingPatternService.getPatternValue(data.patternType, data.patternValue)
            )
            numberingPatternList.add(numberingPatternListDto)
        }
        return numberingPatternList
    }
}
