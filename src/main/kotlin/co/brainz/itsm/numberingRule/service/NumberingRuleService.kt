/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.numberingPattern.constants.NumberingPatternConstants
import co.brainz.itsm.numberingPattern.repository.NumberingPatternRepository
import co.brainz.itsm.numberingRule.dto.NumberingPatternMapDto
import co.brainz.itsm.numberingRule.dto.NumberingRuleDetailDto
import co.brainz.itsm.numberingRule.dto.NumberingRuleDto
import co.brainz.itsm.numberingRule.dto.NumberingRuleListDto
import co.brainz.itsm.numberingRule.dto.NumberingRuleListReturnDto
import co.brainz.itsm.numberingRule.dto.NumberingRuleSearchCondition
import co.brainz.itsm.numberingRule.entity.NumberingRuleEntity
import co.brainz.itsm.numberingRule.entity.NumberingRulePatternMapEntity
import co.brainz.itsm.numberingRule.repository.NumberingRulePatternMapRepository
import co.brainz.itsm.numberingRule.repository.NumberingRuleRepository
import co.brainz.workflow.document.service.WfDocumentService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NumberingRuleService(
    private val codeService: CodeService,
    private val wfDocumentService: WfDocumentService,
    private val numberingRuleRepository: NumberingRuleRepository,
    private val numberingPatternRepository: NumberingPatternRepository,
    private val numberingRulePatternMapRepository: NumberingRulePatternMapRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 전체 문서번호 조회
     */
    fun getNumberingRules(): MutableList<NumberingRuleListDto> {
        val ruleEntities = numberingRuleRepository.findAll()
        val numberingRuleList = mutableListOf<NumberingRuleListDto>()

        for (data in ruleEntities) {
            val numberingRuleListDto = NumberingRuleListDto(
                data.numberingId,
                data.numberingName,
                data.numberingDesc,
                data.latestDate,
                data.latestValue
            )
            numberingRuleList.add(numberingRuleListDto)
        }
        return numberingRuleList
    }

    /**
     * 문서번호 리스트 조회
     */
    fun getNumberingRuleList(numberingRuleSearchCondition: NumberingRuleSearchCondition): NumberingRuleListReturnDto {
        val pagingResult = numberingRuleRepository.findRuleSearch(numberingRuleSearchCondition)
        return NumberingRuleListReturnDto(
            data = mapper.convertValue(pagingResult.dataList),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = numberingRuleRepository.count(),
                currentPageNum = numberingRuleSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / numberingRuleSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code
            )
        )
    }

    /**
     * 문서번호 상세정보 조회
     */
    fun getNumberingRuleDetail(numberingId: String): NumberingRuleDetailDto {
        val ruleDetail = numberingRuleRepository.getOne(numberingId)
        val patternList = mutableListOf<NumberingPatternMapDto>()
        var editable = true

        if (ruleDetail.latestValue != null) {
            editable = false
        }

        ruleDetail.numberingRulePatternMapEntities.forEach {
            patternList.add(
                NumberingPatternMapDto(
                    it.numberingPattern.patternId,
                    it.numberingPattern.patternName,
                    it.numberingPattern.patternType,
                    it.numberingPattern.patternValue,
                    it.patternOrder
                )
            )
        }

        return NumberingRuleDetailDto(
            ruleDetail.numberingId,
            ruleDetail.numberingName,
            ruleDetail.numberingDesc,
            ruleDetail.latestDate,
            ruleDetail.latestValue,
            patternList,
            editable
        )
    }

    /**
     * 문서 번호 등록, 수정
     */
    @Transactional
    fun saveNumberingRule(numberingRuleDto: NumberingRuleDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        var count = 0

        // Duplicate check
        if (this.duplicatePatternCheck(numberingRuleDto)) {
            val numberingRuleEntity = numberingRuleRepository.save(
                NumberingRuleEntity(
                    numberingId = numberingRuleDto.numberingId,
                    numberingName = numberingRuleDto.numberingName,
                    numberingDesc = numberingRuleDto.numberingDesc
                )
            )

            if (numberingRuleDto.numberingId != "") {
                numberingRulePatternMapRepository.deleteByNumberingRule(numberingRuleEntity)
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
        } else {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * 문서 번호 삭제
     */
    @Transactional
    fun deleteNumberingRule(numberingId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        // 1. 업무흐름에 해당 문서번호가 사용중인지 체크
        val documentList = wfDocumentService.getDocumentListByNumberingId(numberingId)
        when (documentList.isNullOrEmpty()) {
            true -> numberingRuleRepository.deleteById(numberingId)
            false -> status = ZResponseConstants.STATUS.ERROR_EXIST
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 문서 번호 생성
     */
    @Transactional
    fun getNewNumbering(numberingId: String): String {
        val numberingRuleEntity = numberingRuleRepository.findById(numberingId)
        var newNumbering = ""
        val currentDateTime = LocalDateTime.now()
        if (numberingRuleEntity.isPresent) {
            val latestValue = numberingRuleEntity.get().latestValue ?: ""
            var latestPatternValues = mutableListOf<String>()
            val newPatternValues = mutableListOf<String>()
            if (latestValue.isNotEmpty()) {
                latestPatternValues = latestValue.split("-") as MutableList<String>
            }
            numberingRuleEntity.get().numberingRulePatternMapEntities.forEachIndexed { index, pattern ->
                var latestPatternValue = ""
                if (latestPatternValues.size > 0 && latestPatternValues.size > +index) {
                    latestPatternValue = latestPatternValues[index]
                }
                if (pattern.numberingPattern.patternValue.isNotEmpty()) {
                    val patternMap = mapper.readValue(pattern.numberingPattern.patternValue, Map::class.java)
                    when (pattern.numberingPattern.patternType) {
                        NumberingPatternConstants.PatternType.TEXT.code -> {
                            newPatternValues.add(getPattenText(patternMap))
                        }
                        NumberingPatternConstants.PatternType.DATE.code -> {
                            newPatternValues.add(
                                getPatternDate(patternMap, currentDateTime)
                            )
                        }
                        NumberingPatternConstants.PatternType.SEQUENCE.code -> {
                            newPatternValues.add(
                                getPatternSequence(
                                    patternMap,
                                    latestPatternValue,
                                    numberingRuleEntity.get().latestDate,
                                    currentDateTime
                                )
                            )
                        }
                    }
                }
                newNumbering = newPatternValues.joinToString(separator = "-")
            }
            // Update Numbering
            numberingRuleEntity.get().latestValue = newNumbering
            numberingRuleEntity.get().latestDate = currentDateTime
            numberingRuleRepository.save(numberingRuleEntity.get())
        }

        return newNumbering
    }

    /**
     * TEXT 패턴 값 추출
     */
    private fun getPattenText(valueMap: Map<*, *>): String {
        return (valueMap[NumberingPatternConstants.ObjProperty.VALUE.property] ?: "") as String
    }

    /**
     * DATE 패턴 값 추출
     */
    private fun getPatternDate(valueMap: Map<*, *>, currentDateTime: LocalDateTime): String {
        var pattern = ""
        val patternCode = (valueMap[NumberingPatternConstants.ObjProperty.CODE.property] ?: "") as String
        val codeList = codeService.selectCodeByParent(NumberingPatternConstants.DEFAULT_DATE_FORMAT_PARENT_CODE)
        codeList.forEach { code ->
            if (code.code == patternCode) {
                pattern = code.codeValue!!
            }
        }
        if (pattern.isEmpty()) {
            pattern = NumberingPatternConstants.DEFAULT_DATE_FORMAT
        }
        val formatter = DateTimeFormatter.ofPattern(pattern)

        return currentDateTime.format(formatter)!!
    }

    /**
     * SEQUENCE 패턴 값 추출
     */
    private fun getPatternSequence(
        valueMap: Map<*, *>,
        latestPatternValue: String,
        latestDate: LocalDateTime?,
        currentDateTime: LocalDateTime
    ): String {
        var latestSequenceValue = this.getLatestSequenceValue(valueMap, latestPatternValue, latestDate, currentDateTime)

        // check digit size and latestPatternValue size
        val digit = (valueMap[NumberingPatternConstants.ObjProperty.DIGIT.property]
            ?: NumberingPatternConstants.DEFAULT_DIGIT) as Int
        if (digit != latestPatternValue.length) {
            latestSequenceValue = 0
        }

        return this.getSequenceValue(valueMap, digit, latestSequenceValue)
    }

    /**
     * 최신 SEQUENCE 값 생성
     */
    private fun getLatestSequenceValue(
        valueMap: Map<*, *>,
        latestPatternValue: String,
        latestDate: LocalDateTime?,
        currentDateTime: LocalDateTime
    ): Int {
        var latestSequenceValue = latestPatternValue.toIntOrNull() ?: 0
        if (latestDate != null) {
            val initialInterval = (valueMap[NumberingPatternConstants.ObjProperty.INITIALINTERVAL.property]
                ?: NumberingPatternConstants.PatternFixedValue.INITIALINTERVAL_KEY.key) as String
            if (initialInterval != NumberingPatternConstants.INTERVAL.NONE.value) {
                val formatPattern = when (initialInterval) {
                    NumberingPatternConstants.INTERVAL.DAY.value -> DateTimeFormatter.ofPattern("yyyyMMdd")
                    NumberingPatternConstants.INTERVAL.MONTH.value -> DateTimeFormatter.ofPattern("yyyyMM")
                    NumberingPatternConstants.INTERVAL.YEAR.value -> DateTimeFormatter.ofPattern("yyyy")
                    else -> DateTimeFormatter.ofPattern("yyyyMMdd")
                }
                if (currentDateTime.format(formatPattern) != latestDate.format(formatPattern)) {
                    latestSequenceValue = 0
                }
            }
        }

        return latestSequenceValue
    }

    /**
     * SEQUENCE 값 생성
     */
    private fun getSequenceValue(valueMap: Map<*, *>, digit: Int, latestSequenceValue: Int): String {
        val startWith = (valueMap[NumberingPatternConstants.ObjProperty.STARTWITH.property]
            ?: NumberingPatternConstants.PatternFixedValue.STARTWITH_KEY.key) as Int
        var value = when (latestSequenceValue) {
            0 -> startWith.toString()
            else -> (latestSequenceValue + 1).toString()
        }
        // check over digit
        if (digit < value.length) {
            value = "1"
        }

        when (((valueMap[NumberingPatternConstants.ObjProperty.FULLFILL.property]
            ?: NumberingPatternConstants.PatternFixedValue.FULLFILL_KEY.key) as String == "Y")) {
            true -> {
                value = value.padStart(digit, '0')
            }
        }

        return value
    }

    /**
     * 문서번호 등록/수정 시 패턴 중복 체크
     */
    fun duplicatePatternCheck(numberingRuleDto: NumberingRuleDto): Boolean {
        val numberingRulePatternMapResult = numberingRulePatternMapRepository.findAllByNumberingPatternIn(
            numberingRuleDto.patternList,
            numberingRuleDto.numberingId
        )
        if (numberingRulePatternMapResult.isNotEmpty()) {
            val numberingGrouping = numberingRulePatternMapResult.groupBy { it.numberingRule }
            for (numbering in numberingGrouping) {
                var order = 0
                val numberingGroupingList = numbering.value
                for (pattern in numberingGroupingList) {
                    if (numberingRuleDto.patternList[order] == pattern.numberingPattern.patternId && order == pattern.patternOrder) {
                        order++
                    }
                }
                if (numberingRuleDto.patternList.size == order) {
                    return false
                }
            }
        }
        return true
    }
}
