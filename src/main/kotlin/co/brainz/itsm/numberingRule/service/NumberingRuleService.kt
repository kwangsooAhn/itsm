package co.brainz.itsm.numberingRule.service

import co.brainz.itsm.numberingPattern.dto.NumberingPatternDetailDto
import co.brainz.itsm.numberingPattern.repository.NumberingPatternRepository
import co.brainz.itsm.numberingRule.constants.NumberingRuleConstants
import co.brainz.itsm.numberingRule.dto.NumberingRuleDetailDto
import co.brainz.itsm.numberingRule.dto.NumberingRuleDto
import co.brainz.itsm.numberingRule.dto.NumberingRuleListDto
import co.brainz.itsm.numberingRule.entity.NumberingRuleEntity
import co.brainz.itsm.numberingRule.entity.NumberingRulePatternMapEntity
import co.brainz.itsm.numberingRule.entity.NumberingRulePatternMapPk
import co.brainz.itsm.numberingRule.repository.NumberingRulePatternMapRepository
import co.brainz.itsm.numberingRule.repository.NumberingRuleRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NumberingRuleService(
    private val numberingRuleRepository: NumberingRuleRepository,
    private val numberingPatternRepository: NumberingPatternRepository,
    private val numberingRulePatternMapRepository: NumberingRulePatternMapRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 문서번호 리스트 조회
     */
    fun getNumberingRuleList(search: String): MutableList<NumberingRuleListDto> {

        return numberingRuleRepository.findRuleSearch(search)
    }

    /**
     * 문서번호 상세정보 조회
     */
    fun getNumberingRuleDetail(numberingId: String): NumberingRuleDetailDto {
        val ruleDetail = numberingRuleRepository.getOne(numberingId)
        val patternList = mutableListOf<NumberingPatternDetailDto>()

        ruleDetail.numberingRulePatternMapEntities.forEach {
            patternList.add(
                NumberingPatternDetailDto(
                    it.numberingPattern.patternId,
                    it.numberingPattern.patternName,
                    it.numberingPattern.patternType,
                    it.numberingPattern.patternValue
                )
            )
        }

        return NumberingRuleDetailDto(
            ruleDetail.numberingId,
            ruleDetail.numberingName,
            ruleDetail.numberingDesc,
            ruleDetail.latestDate,
            ruleDetail.latestValue,
            patternList
        )
    }

    /**
     * 문서 번호 등록, 수정
     */
    fun saveNumberingRule(numberingRuleDto: NumberingRuleDto): String {
        val status = NumberingRuleConstants.Status.STATUS_SUCCESS.code
        var count = 0

        val numberingRuleEntity = numberingRuleRepository.save(
            NumberingRuleEntity(
                numberingId = numberingRuleDto.numberingId,
                numberingName = numberingRuleDto.numberingName,
                numberingDesc = numberingRuleDto.numberingDesc
            )
        )

        if (numberingRuleDto.numberingId != "") {
            numberingRuleEntity.numberingRulePatternMapEntities.forEach {
                numberingRulePatternMapRepository.deleteById(
                    NumberingRulePatternMapPk(
                        numberingRuleEntity.numberingId,
                        it.numberingPattern.patternId,
                        count
                    )
                )
                count++
            }
            count = 0
        }

        numberingRuleDto.patternList.forEach {
            val numberingPatternEntity = numberingPatternRepository.getOne(it)
            numberingRulePatternMapRepository.save(
                NumberingRulePatternMapEntity(
                    numberingRuleEntity,
                    numberingPatternEntity,
                    count
                )
            )
            count++
        }

        return status
    }

    /**
     * 문서 번호 삭제
     */
    @Transactional
    fun deleteNumberingRule(numberingId: String): String {
        val status = NumberingRuleConstants.Status.STATUS_SUCCESS.code
        numberingRuleRepository.deleteById(numberingId)

        return status
    }
}
