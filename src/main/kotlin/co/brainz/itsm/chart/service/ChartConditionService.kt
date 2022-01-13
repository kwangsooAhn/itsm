package co.brainz.itsm.chart.service

import co.brainz.itsm.chart.constants.ChartConditionConstants
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ChartConditionService() {

    val condition = " (  [완료 희망일][완료 희망일] - [완료 일][완료 희망일] >= -5 || [완료 희망일] - [완료 일] >= 1   ) "
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 메인 함수
     */
    fun executeCondition(): String {
        val chartCondition = this.preVerification(condition)
        chartCondition.let { condition ->
            val binaryTree = this.createBinaryTree(condition)
        }


        return ""
    }

    /**
     * [step 2] 조건식 이진 트리 생성
     */
    private fun createBinaryTree(chartCondition: String?): String {
        return ""
    }

    /**
     * 이진 트리 데이터 수집
     */
    fun collectionBinaryTreeData(): String {
        return ""
    }


    /**
     * [step 1] 조건식 데이터 파싱
     * 조건식 전체가 소괄호가 적용되어 있는 경우 제거를 진행한다.
     * 조건식에서 대괄호로 처리된 부분을 제외하고 공백을 제거한다.
     * */
    private fun preVerification(condition: String): String? {
        return if (condition.isNotBlank()) {
            // 앞뒤 공백 제거 및 조건식 전체가 소괄호가 적용되어 있는 경우, 공백 제거 및 소괄호 제거를 진행한다.
            var parsingCondition = condition.trim()
            if (parsingCondition.startsWith(ChartConditionConstants.parentheses.prefixParentheses.value) &&
                parsingCondition.endsWith(ChartConditionConstants.parentheses.suffixParentheses.value)
            ) {
                parsingCondition = parsingCondition.removeSurrounding(
                    ChartConditionConstants.parentheses.prefixParentheses.value.toString(),
                    ChartConditionConstants.parentheses.suffixParentheses.value.toString()
                )
            }

            // 문자열에서 태그 선언 부분을 제외한 모든 공백에 대한 제거를 진행한다.
            var startIndex = 0
            var returnCondition = ""
            println(parsingCondition.indices.last)
            while (startIndex < parsingCondition.length) {
                if (parsingCondition[startIndex] == ChartConditionConstants.parentheses.prefixSquareBrackets.value) {
                    for (index in startIndex..parsingCondition.indices.last) {
                        if (parsingCondition[index] == ChartConditionConstants.parentheses.suffixSquareBrackets.value) {
                            returnCondition = returnCondition.plus(parsingCondition.substring(startIndex, index + 1))
                            startIndex = index + 1
                            break
                        }
                    }
                    continue
                }
                println(parsingCondition[startIndex])
                if (parsingCondition[startIndex].toString().isNotBlank()) {
                    returnCondition = returnCondition.plus(parsingCondition[startIndex])
                }
                startIndex++
            }
            returnCondition
        } else {
            null
        }
    }

}