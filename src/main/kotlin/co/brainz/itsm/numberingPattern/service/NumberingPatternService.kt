package co.brainz.itsm.numberingPattern.service

import co.brainz.itsm.numberingPattern.constants.NumberingPatternConstants
import co.brainz.itsm.numberingPattern.dto.NumberingPatternDetailDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternListDto
import co.brainz.itsm.numberingPattern.entity.NumberingPatternEntity
import co.brainz.itsm.numberingPattern.repository.NumberingPatternRepository
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NumberingPatternService(private val numberingPatternRepository: NumberingPatternRepository) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 패턴 리스트 조회
     */
    fun getNumberingPatternList(search: String): MutableList<NumberingPatternListDto> {

        return numberingPatternRepository.findPatternSearch(search)
    }

    /**
     * 패턴 상세정보 조회
     */
    fun getNumberingPatternsDetail(patternId: String): NumberingPatternDetailDto {
        val patternDetail = numberingPatternRepository.getOne(patternId)
        var editable = true;

        if (patternDetail.numberingRulePatternMapEntities.size > 0) {
            editable = false;
        }

        return NumberingPatternDetailDto(
            patternDetail.patternId,
            patternDetail.patternName,
            patternDetail.patternType,
            getPatternValue(patternDetail.patternType, patternDetail.patternValue),
            editable
        )
    }

    /**
     * 패턴 정보 등록, 수정
     */
    @Transactional
    fun saveNumberingPattern(numberingPatternDto: NumberingPatternDto): String {
        var status = NumberingPatternConstants.Status.STATUS_SUCCESS.code
        val patternValueObj = JsonObject()
        val patternType = numberingPatternDto.patternType
        val patternValue = numberingPatternDto.patternValue

        when (patternType) {
            NumberingPatternConstants.PatternType.TEXT.code -> {
                patternValueObj.addProperty(NumberingPatternConstants.ObjProperty.VALUE.property, patternValue)
            }
            NumberingPatternConstants.PatternType.DATE.code -> {
                patternValueObj.addProperty(NumberingPatternConstants.ObjProperty.CODE.property, patternValue)
            }
            NumberingPatternConstants.PatternType.SEQUENCE.code -> {
                patternValueObj.addProperty(NumberingPatternConstants.ObjProperty.DIGIT.property, patternValue.toInt())
                patternValueObj.addProperty(
                    NumberingPatternConstants.ObjProperty.STARTWITH.property,
                    NumberingPatternConstants.PatternFixedValue.STARTWITH_KEY.key.toInt()
                )
                patternValueObj.addProperty(
                    NumberingPatternConstants.ObjProperty.FULLFILL.property,
                    NumberingPatternConstants.PatternFixedValue.FULLFILL_KEY.key
                )
                patternValueObj.addProperty(
                    NumberingPatternConstants.ObjProperty.INITIALINTERVAL.property,
                    NumberingPatternConstants.PatternFixedValue.INITIALINTERVAL_KEY.key
                )
            }
        }

        val numberingPatternEntity = NumberingPatternEntity(
            numberingPatternDto.patternId,
            numberingPatternDto.patternName,
            numberingPatternDto.patternType,
            patternValueObj.toString()
        )

        when (numberingPatternEntity.patternId != "" && numberingPatternRepository.getOne(numberingPatternEntity.patternId).numberingRulePatternMapEntities.size > 0) {
            true -> {
                status = NumberingPatternConstants.Status.STATUS_ERROR_PATTERN_USED.code
            }
            false -> {
                numberingPatternRepository.save(numberingPatternEntity)
            }
        }
        return status
    }

    /**
     * 패턴 정보 삭제
     */
    fun deleteNumberingPattern(patternId: String): String {
        var status = NumberingPatternConstants.Status.STATUS_SUCCESS.code

        when (numberingPatternRepository.getOne(patternId).numberingRulePatternMapEntities.size > 0) {
            true -> {
                status = NumberingPatternConstants.Status.STATUS_ERROR_PATTERN_USED.code
            }
            false -> {
                numberingPatternRepository.deleteById(patternId)
            }
        }
        return status
    }

    fun getPatternValue(patternType: String, originPatternValue: String): String {
        val jsonParser = JsonParser()
        var objProperty = ""
        var patternValue = ""
        when (patternType) {
            NumberingPatternConstants.PatternType.TEXT.code -> {
                objProperty = NumberingPatternConstants.ObjProperty.VALUE.property
                patternValue = jsonParser.parse(originPatternValue).asJsonObject.get(objProperty).asString
            }
            NumberingPatternConstants.PatternType.DATE.code -> {
                objProperty = NumberingPatternConstants.ObjProperty.CODE.property
                patternValue = jsonParser.parse(originPatternValue).asJsonObject.get(objProperty).asString

                when (patternValue) {
                    NumberingPatternConstants.PatternDateValue.YYYYMMDD.code -> {
                        patternValue = NumberingPatternConstants.PatternDateValue.SUMMARIZE_YYYYMMDD.code
                    }
                    NumberingPatternConstants.PatternDateValue.YYYYDDMM.code -> {
                        patternValue = NumberingPatternConstants.PatternDateValue.SUMMARIZE_YYYYDDMM.code
                    }
                    NumberingPatternConstants.PatternDateValue.DDMMYYYY.code -> {
                        patternValue = NumberingPatternConstants.PatternDateValue.SUMMARIZE_DDMMYYYY.code
                    }
                    NumberingPatternConstants.PatternDateValue.MMDDYYYY.code -> {
                        patternValue = NumberingPatternConstants.PatternDateValue.SUMMARIZE_MMDDYYYY.code
                    }
                }
            }
            NumberingPatternConstants.PatternType.SEQUENCE.code -> {
                objProperty = NumberingPatternConstants.ObjProperty.DIGIT.property
                patternValue = jsonParser.parse(originPatternValue).asJsonObject.get(objProperty).asString
            }
        }
        return patternValue
    }

    fun getPatternNameList(): MutableList<NumberingPatternListDto> {
        val patternEntities = numberingPatternRepository.findAll()
        var numberingPatternList = mutableListOf<NumberingPatternListDto>()

        for (data in patternEntities) {
            val numberingPatternListDto = NumberingPatternListDto(
                data.patternId,
                data.patternName,
                data.patternType,
                data.patternValue
            )
            numberingPatternList.add(numberingPatternListDto)
        }
        return numberingPatternList
    }
}
