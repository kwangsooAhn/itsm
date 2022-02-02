package co.brainz.itsm.statistic.customChart.service

import co.brainz.itsm.statistic.customChart.constants.ChartConditionConstants
import co.brainz.itsm.statistic.customChart.dto.ChartConditionNode
import co.brainz.itsm.statistic.customChart.dto.ChartDto
import co.brainz.itsm.statistic.customChart.dto.ChartTagInstanceDto
import java.util.Stack
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ChartConditionService(private val chartExpressionTree: ChartExpressionTree) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    var data: String? = null
    var chartCondition: String = ""
    var root: ChartConditionNode? = null

    /**
     * 메인 함수
     */
    fun executeCondition(chartDto: ChartDto, tagInstanceList: List<ChartTagInstanceDto>): List<ChartTagInstanceDto> {

        return chartExpressionTree.execute(chartDto, tagInstanceList)
    }

    /**
     * 조건식 소괄호 제거
     *
     **/
    private fun removeParentheses(condition: String): String {
        var parsingCondition = condition.trim()
        if (condition.isNotBlank()) {
            if (parsingCondition.startsWith(ChartConditionConstants.Parentheses.PREFIX_PARENTHESES.value) &&
                parsingCondition.endsWith(ChartConditionConstants.Parentheses.SUFFIX_PARENTHESES.value)
            ) {
                parsingCondition = parsingCondition.removeSurrounding(
                    ChartConditionConstants.Parentheses.PREFIX_PARENTHESES.value,
                    ChartConditionConstants.Parentheses.SUFFIX_PARENTHESES.value
                )
            }
        }

        return parsingCondition
    }

    /**
     * 조건식 내부 공백 제거
     *
     * */
    private fun removeSpace(condition: String): String {
        var parsingCondition = ""
        if (condition.isNotBlank()) {
            var startIndex = 0
            val condition = condition.trim()
            while (startIndex < condition.length) {
                if (condition[startIndex].toString() == ChartConditionConstants.Parentheses.PREFIX_SQUARE_BRACKETS.value) {
                    for (index in startIndex..condition.indices.last) {
                        if (condition[index].toString() == ChartConditionConstants.Parentheses.SUFFIX_SQUARE_BRACKETS.value) {
                            parsingCondition =
                                parsingCondition.plus(condition.substring(startIndex, index + 1))
                            startIndex = index + 1
                            break
                        }
                    }
                    continue
                }
                if (condition[startIndex].toString().isNotBlank()) {
                    parsingCondition = parsingCondition.plus(condition[startIndex])
                }
                startIndex++
            }
        }

        return parsingCondition
    }

    /**
     * 조건식 소괄호 및 대괄호 검사
     *
     **/
    private fun parenthesesInspection(condition: String): Boolean {
        val prefixParentheses = ChartConditionConstants.Parentheses.PREFIX_PARENTHESES.value.single()
        val suffixParentheses = ChartConditionConstants.Parentheses.SUFFIX_PARENTHESES.value.single()
        val prefixSquareBrackets = ChartConditionConstants.Parentheses.PREFIX_SQUARE_BRACKETS.value.single()
        val suffixSquareBrackets = ChartConditionConstants.Parentheses.SUFFIX_SQUARE_BRACKETS.value.single()
        val stack = Stack<Char>()
        var testCh: Char
        var openPair: Char

        for (index in condition) {
            testCh = index
            when (testCh) {
                prefixParentheses,
                prefixSquareBrackets
                -> stack.push(testCh)
                suffixParentheses,
                suffixSquareBrackets
                -> if (stack.isEmpty()) {
                    return false
                } else {
                    openPair = stack.pop()
                    if ((testCh !== prefixParentheses) && (openPair === suffixParentheses) ||
                        (testCh !== prefixSquareBrackets) && (openPair === suffixSquareBrackets)
                    ) {
                        return false
                    }
                }
            }
        }

        return stack.isEmpty()
    }
}