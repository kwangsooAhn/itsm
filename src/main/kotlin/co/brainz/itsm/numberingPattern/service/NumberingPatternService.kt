/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingPattern.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.numberingPattern.constants.NumberingPatternConstants
import co.brainz.itsm.numberingPattern.dto.NumberingPatternDetailDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternListDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternListReturnDto
import co.brainz.itsm.numberingPattern.dto.NumberingPatternSearchCondition
import co.brainz.itsm.numberingPattern.entity.NumberingPatternEntity
import co.brainz.itsm.numberingPattern.repository.NumberingPatternRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlin.math.ceil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NumberingPatternService(private val numberingPatternRepository: NumberingPatternRepository) {

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 패턴 리스트 조회
     */
    fun getNumberingPatternList(numberingPatternSearchCondition: NumberingPatternSearchCondition): NumberingPatternListReturnDto {
        val pagingResult = numberingPatternRepository.findPatternSearch(numberingPatternSearchCondition)
        return NumberingPatternListReturnDto(
            data = mapper.convertValue(pagingResult.dataList),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = numberingPatternRepository.count(),
                currentPageNum = numberingPatternSearchCondition.pageNum,
                totalPageNum = ceil(
                    pagingResult.totalCount.toDouble() / numberingPatternSearchCondition.contentNumPerPage.toDouble()
                ).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code
            )
        )
    }

    /**
     * 패턴 상세정보 조회
     */
    fun getNumberingPatternsDetail(patternId: String): NumberingPatternDetailDto {
        val patternDetail = numberingPatternRepository.getOne(patternId)
        var editable = true

        if (patternDetail.numberingRulePatternMapEntities.size > 0) {
            editable = false
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
     * 패턴 정보 등록
     */
    @Transactional
    fun insertNumberingPattern(numberingPatternDto: NumberingPatternDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val patternEntity = NumberingPatternEntity(
            patternId = numberingPatternDto.patternId,
            patternName = numberingPatternDto.patternName,
            patternType = numberingPatternDto.patternType,
            patternValue = this.makePatternObject(numberingPatternDto)
        )

        when (numberingPatternRepository.existsByPatternName(patternEntity.patternName)) {
            true -> {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
            false -> {
                numberingPatternRepository.save(patternEntity)
            }
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * 패턴 정보 수정
     */
    @Transactional
    fun updateNumberingPattern(numberingPatternDto: NumberingPatternDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val numberingPatternEntity = NumberingPatternEntity(
            patternId = numberingPatternDto.patternId,
            patternName = numberingPatternDto.patternName,
            patternType = numberingPatternDto.patternType,
            patternValue = this.makePatternObject(numberingPatternDto)
        )
        when (numberingPatternRepository.getOne(numberingPatternEntity.patternId).numberingRulePatternMapEntities.size > 0) {
            true -> {
                status = ZResponseConstants.STATUS.ERROR_EXIST
            }
            false -> {
                val existsPattern = numberingPatternRepository.getOne(numberingPatternEntity.patternId)
                val isExistPattern = existsPattern.patternName == numberingPatternEntity.patternName
                if (isExistPattern) {
                    numberingPatternRepository.save(numberingPatternEntity)
                } else {
                    if (numberingPatternRepository.existsByPatternName(numberingPatternEntity.patternName)) {
                        status = ZResponseConstants.STATUS.ERROR_DUPLICATE
                    } else {
                        numberingPatternRepository.save(numberingPatternEntity)
                    }
                }
            }
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * 패턴 정보 삭제
     */
    @Transactional
    fun deleteNumberingPattern(patternId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        // 패턴 삭제 가능 여부 확인
        when (numberingPatternRepository.getOne(patternId).numberingRulePatternMapEntities.size > 0) {
            true -> {
                status = ZResponseConstants.STATUS.ERROR_EXIST
            }
            false -> {
                numberingPatternRepository.deleteById(patternId)
            }
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 패턴 타입에 따른 값 추출
     */
    fun getPatternValue(patternType: String, originPatternValue: String): String {
        val jsonParser = JsonParser()
        val objProperty: String
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

    /**
     * 패턴 이름 리스트 출력
     */
    fun getPatternNameList(): MutableList<NumberingPatternListDto> {
        val patternEntities = numberingPatternRepository.findAll()
        val numberingPatternList = mutableListOf<NumberingPatternListDto>()

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

    /**
     * 패턴 Object 생성
     */
    private fun makePatternObject(patternDto: NumberingPatternDto): String {
        val patternValueObj = JsonObject()
        val patternType = patternDto.patternType
        val patternValue = patternDto.patternValue

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

        return patternValueObj.toString()
    }
}
