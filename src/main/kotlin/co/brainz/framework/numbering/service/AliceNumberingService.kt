package co.brainz.framework.numbering.service

import co.brainz.itsm.code.service.CodeService
import co.brainz.framework.numbering.constants.AliceNumberingConstants
import co.brainz.framework.numbering.dto.AliceNumberingRuleDto
import co.brainz.framework.numbering.mapper.AliceNumberingRuleMapper
import co.brainz.framework.numbering.repository.AliceNumberingRuleRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class AliceNumberingService(
    private val aliceNumberingRuleRepository: AliceNumberingRuleRepository,
    private val codeService: CodeService
) {

    private val aliceNumberingRuleMapper = Mappers.getMapper(AliceNumberingRuleMapper::class.java)
    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Get Numbering Rule.
     */
    fun getNumberingRules(): MutableList<AliceNumberingRuleDto> {
        val aliceNumberingRules: MutableList<AliceNumberingRuleDto> = mutableListOf()
        val aliceNumberingRuleEntities = aliceNumberingRuleRepository.findAll()
        for (aliceNumberingRule in aliceNumberingRuleEntities) {
            aliceNumberingRules.add(aliceNumberingRuleMapper.toAliceNumberingRuleDto(aliceNumberingRule))
        }

        return aliceNumberingRules
    }

    /**
     * Get New Numbering.
     *
     * @param numberingId
     * @return String
     */
    fun getNewNumbering(numberingId: String): String {
        val aliceNumberingRuleEntity = aliceNumberingRuleRepository.findById(numberingId)
        var newNumbering = ""
        val currentDateTime = LocalDateTime.now()
        if (aliceNumberingRuleEntity.isPresent) {
            val latestValue = aliceNumberingRuleEntity.get().latestValue ?: ""
            var latestPatternValues: MutableList<String> = mutableListOf()
            val newPatternValues: MutableList<String> = mutableListOf()
            if (latestValue.isNotEmpty()) {
                latestPatternValues = latestValue.split("-") as MutableList<String>
            }
            aliceNumberingRuleEntity.get().patterns?.forEachIndexed { index, pattern ->
                var latestPatternValue = ""
                if (latestPatternValues.size > 0 && latestPatternValues.size >= index) {
                    latestPatternValue = latestPatternValues[index]
                }
                if (pattern.patternValue.isNotEmpty()) {
                    val patternMap = mapper.readValue(pattern.patternValue, Map::class.java)
                    when (pattern.patternType) {
                        AliceNumberingConstants.PatternType.TEXT.code -> newPatternValues.add(getPattenText(patternMap))
                        AliceNumberingConstants.PatternType.DATE.code ->
                            newPatternValues.add(
                                getPatternDate(patternMap, currentDateTime)
                            )
                        AliceNumberingConstants.PatternType.SEQUENCE.code ->
                            newPatternValues.add(
                                getPatternSequence(
                                    patternMap,
                                    latestPatternValue,
                                    aliceNumberingRuleEntity.get().latestDate,
                                    currentDateTime
                                )
                            )
                    }
                }
                newNumbering = newPatternValues.joinToString(separator = "-")
            }
            //Update Numbering
            aliceNumberingRuleEntity.get().latestValue = newNumbering
            aliceNumberingRuleEntity.get().latestDate = currentDateTime
            aliceNumberingRuleRepository.save(aliceNumberingRuleEntity.get())
        }

        return newNumbering
    }

    /**
     * Pattern: Text.
     *
     * @param valueMap
     * @return String
     */
    private fun getPattenText(valueMap: Map<*, *>): String {
        return (valueMap[AliceNumberingConstants.PatternValueId.TEXT_VALUE.value] ?: "") as String
    }

    /**
     * Pattern: Date.
     *
     * @param valueMap
     * @param currentDateTime
     * @return String
     */
    private fun getPatternDate(valueMap: Map<*, *>, currentDateTime: LocalDateTime): String {
        var pattern = ""
        val patternCode = (valueMap[AliceNumberingConstants.PatternValueId.DATE_CODE.value] ?: "") as String
        val codeList = codeService.selectCodeByParent(AliceNumberingConstants.DEFAULT_DATE_FORMAT_PARENT_CODE)
        codeList.forEach { code ->
            if (code.code == patternCode) {
                pattern = code.codeValue!!
            }
        }
        if (pattern.isEmpty()) {
            pattern = AliceNumberingConstants.DEFAULT_DATE_FORMAT
        }
        val formatter = DateTimeFormatter.ofPattern(pattern)

        return currentDateTime.format(formatter)!!
    }

    /**
     * Pattern: Sequence.
     *
     * @param valueMap
     * @param latestPatternValue
     * @param latestDate
     * @param currentDateTime
     * @return String
     */
    private fun getPatternSequence(
        valueMap: Map<*, *>,
        latestPatternValue: String,
        latestDate: LocalDateTime?,
        currentDateTime: LocalDateTime
    ): String {
        val digit = (valueMap[AliceNumberingConstants.PatternValueId.SEQUENCE_DIGIT.value]
            ?: AliceNumberingConstants.DEFAULT_DIGIT) as Int
        var latestSequenceValue = latestPatternValue.toIntOrNull() ?: 0

        //check init
        if (latestDate != null) {
            val initialInterval = (valueMap[AliceNumberingConstants.PatternValueId.SEQUENCE_INITIAL_INTERVAL.value]
                ?: AliceNumberingConstants.DEFAULT_INITIAL_INTERVAL) as String
            val formatPattern = when (initialInterval) {
                AliceNumberingConstants.INTERVAL.DAY.value -> DateTimeFormatter.ofPattern("yyyyMMdd")
                AliceNumberingConstants.INTERVAL.MONTH.value -> DateTimeFormatter.ofPattern("yyyyMM")
                AliceNumberingConstants.INTERVAL.YEAR.value -> DateTimeFormatter.ofPattern("yyyy")
                else -> DateTimeFormatter.ofPattern("yyyyMMdd")
            }
            if (currentDateTime.format(formatPattern) != latestDate.format(formatPattern)) {
                latestSequenceValue = 0
            }
        }

        //check digit size and latestPatternValue size
        if (digit != latestPatternValue.length) {
            latestSequenceValue = 0
        }
        val startWith = (valueMap[AliceNumberingConstants.PatternValueId.SEQUENCE_START_WITH.value]
            ?: AliceNumberingConstants.DEFAULT_START_WITH) as Int
        var value = when (latestSequenceValue) {
            0 -> startWith.toString()
            else -> (latestSequenceValue + 1).toString()
        }
        //check over digit
        if (digit < value.length) {
            value = "1"
        }

        when (((valueMap[AliceNumberingConstants.PatternValueId.SEQUENCE_FULL_FILL.value]
            ?: AliceNumberingConstants.DEFAULT_FUL_FILL) as String == "Y")) {
            true -> {
                value = value.padStart(digit, '0')
            }
        }

        return value
    }

}
