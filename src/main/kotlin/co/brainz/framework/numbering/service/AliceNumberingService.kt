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
                        AliceNumberingConstants.PatternType.DATE.code -> newPatternValues.add(getPatternDate(patternMap))
                        AliceNumberingConstants.PatternType.SEQUENCE.code -> newPatternValues.add(
                            getPatternSequence(
                                patternMap,
                                latestPatternValue
                            )
                        )
                    }
                }
                newNumbering = newPatternValues.joinToString(separator = "-")
            }
            //Update Numbering
            aliceNumberingRuleEntity.get().latestValue = newNumbering
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
     * @return String
     */
    private fun getPatternDate(valueMap: Map<*, *>): String {
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
        val digit = (valueMap[AliceNumberingConstants.PatternValueId.SEQUENCE_DIGIT.value]
            ?: AliceNumberingConstants.DEFAULT_DIGIT) as Int
        var latestSequenceValue = latestPatternValue.toIntOrNull() ?: 0
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
