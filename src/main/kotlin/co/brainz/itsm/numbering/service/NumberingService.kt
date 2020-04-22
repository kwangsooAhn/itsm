package co.brainz.itsm.numbering.service

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.numbering.constants.NumberingConstants
import co.brainz.itsm.numbering.repository.NumberingRuleRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class NumberingService(private val numberingRuleRepository: NumberingRuleRepository,
                       private val codeService: CodeService) {

    private val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Get New Numbering.
     *
     * @param numberingId
     * @return String
     */
    fun getNewNumbering(numberingId: String): String {
        val numberingRuleEntity = numberingRuleRepository.findById(numberingId)
        var newNumbering = ""
        if (numberingRuleEntity.isPresent) {
            val latestValue = numberingRuleEntity.get().latestValue?:""
            var latestPatternValues: MutableList<String> = mutableListOf()
            val newPatternValues: MutableList<String> = mutableListOf()
            if (latestValue.isNotEmpty()) {
                latestPatternValues = latestValue.split("-") as MutableList<String>
            }
            numberingRuleEntity.get().patterns?.forEachIndexed { index, pattern ->
                var latestPatternValue = ""
                if (latestPatternValues.size > 0 && latestPatternValues.size >= index) {
                    latestPatternValue = latestPatternValues[index]
                }
                if (pattern.patternValue.isNotEmpty()) {
                    val patternMap = mapper.readValue(pattern.patternValue, Map::class.java)
                    when (pattern.patternType) {
                        NumberingConstants.PatternType.TEXT.code -> newPatternValues.add(getPattenText(patternMap))
                        NumberingConstants.PatternType.DATE.code -> newPatternValues.add(getPatternDate(patternMap))
                        NumberingConstants.PatternType.SEQUENCE.code -> newPatternValues.add(getPatternSequence(patternMap, latestPatternValue))
                    }
                }
                newNumbering = newPatternValues.joinToString(separator = "-")
            }
            //Update Numbering
            numberingRuleEntity.get().latestValue = newNumbering
            numberingRuleRepository.save(numberingRuleEntity.get())
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
        return (valueMap[NumberingConstants.PatternValueId.TEXT_VALUE.value]?:"") as String
    }

    /**
     * Pattern: Date.
     *
     * @param valueMap
     * @return String
     */
    private fun getPatternDate(valueMap: Map<*, *>): String {
        var pattern = ""
        val patternCode = (valueMap[NumberingConstants.PatternValueId.DATE_CODE.value]?:"") as String
        val codeList = codeService.selectCodeByParent(NumberingConstants.DEFAULT_DATE_FORMAT_PARENT_CODE)
        codeList.forEach { code ->
            if (code.code == patternCode) {
                pattern = code.codeValue!!
            }
        }
        if (pattern.isEmpty()) {
            pattern = NumberingConstants.DEFAULT_DATE_FORMAT
        }
        val formatter = DateTimeFormatter.ofPattern(pattern)

        return LocalDateTime.now().format(formatter)!!
    }

    /**
     * Pattern: Sequence.
     *
     * @param valueMap
     * @param latestPatternValue
     * @return String
     */
    private fun getPatternSequence(valueMap: Map<*, *>, latestPatternValue: String): String {
        var value = ""
        val digit = (valueMap[NumberingConstants.PatternValueId.SEQUENCE_DIGIT.value]
                ?:NumberingConstants.DEFAULT_DIGIT) as Int
        var latestSequenceValue = latestPatternValue.toIntOrNull()?:0
        //check digit size and latestPatternValue size
        if (digit != latestPatternValue.length) {
            latestSequenceValue = 0
        }
        val startWith = (valueMap[NumberingConstants.PatternValueId.SEQUENCE_START_WITH.value]
                ?:NumberingConstants.DEFAULT_START_WITH) as Int
        value = when (latestSequenceValue) {
            0 -> startWith.toString()
            else -> (latestSequenceValue + 1).toString()
        }
        //check over digit
        if (digit < value.length) {
            value = "1"
        }

        when (((valueMap[NumberingConstants.PatternValueId.SEQUENCE_FULL_FILL.value]
                ?:NumberingConstants.DEFAULT_FUL_FILL) as String == "Y")) {
            true -> {
                value = value.padStart(digit, '0')
            }
        }

        return value
    }

}
