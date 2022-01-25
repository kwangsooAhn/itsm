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
    fun executeCondition(chartDto: ChartDto, tagInstanceList: List<ChartTagInstanceDto>): String {
        val te3 = chartExpressionTree.execute("1+1", tagInstanceList)
        val test102 = chartExpressionTree.execute("100-100", tagInstanceList)
        val test103 = chartExpressionTree.execute("-100-100", tagInstanceList)
        val test105 = chartExpressionTree.execute("-500*-500", tagInstanceList)
        val test106 = chartExpressionTree.execute("(-500*-500)", tagInstanceList)
        val test111 = chartExpressionTree.execute("(-900+800)*-1", tagInstanceList)
        val test112 = chartExpressionTree.execute("(-900*800)*-1", tagInstanceList)
        val test113 = chartExpressionTree.execute("(-900*800)*1", tagInstanceList)
        val test114 = chartExpressionTree.execute("-1-1*3", tagInstanceList)
        val test99 = chartExpressionTree.execute("[a]+[b]", tagInstanceList)
        val test104 = chartExpressionTree.execute( "\"test\"+\"test1\"", tagInstanceList)
        return ""
    }

    /**
     * [step 2] 조건식 이진 트리 생성
     */
    private fun createBinaryTree(): String {
        return ""
    }


    /**
     * 이진 트리 데이터 수집
     * 데이터 수집은 후위 순회 방식으로 인스턴스 리스트 수집을 진행한다
     */
    fun collectBinaryTreeData(): String {
        return ""
    }


    /**
     * 생성된 조건식 이진트리에 대하여 검증을 진행한다. (중위 순회)
     *
     */
/*
    private fun inOrderVerification(condition: String, root: ChartConditionNode): Boolean {
        val inOrderData = this.inOrder(root)
        return if (condition == inOrderData) {
            data = ""
            true
        } else {
            false
        }
    }
*/

    /*  */
    /**
     * 중위 순회 탐색 진행 (inOrder)
     *
     *//*
    private fun inOrder(root: ChartConditionNode): String? {
        if (root.leftNode != null) {
            inOrder(root.leftNode!!)
        }
        if (!root.data?.value.isNullOrBlank()) {
            data += root.data?.value
        }
        if (root.rightNode != null) {
            inOrder(root.rightNode!!)
        }
        return data
    }
*/

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