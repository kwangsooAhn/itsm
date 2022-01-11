package co.brainz.itsm.chart.service

class ChartCondition(
    private val chartService: ChartService
) {
    fun conditionParsing() {
        val condition = "[완료희망일] - [완료일] >= -5 || [완료희망일] - [완료일] >= 1"
        this.conditionTypeOrNullCheck(condition = condition)
        if (!condition.isNullOrBlank() && condition is String) {
            condition.replace(" ", "")
        }
    }


    private fun conditionTypeOrNullCheck(condition: String): String? {
        val target = condition.replace(" ", "")
        if (!target.isNullOrBlank() && target is String) {
            return condition
        }
        return null
    }

    /**
     * 조건문 소괄호('()') 체크 진행
     */
    private fun conditionParenthesesCheck(condition: String): String? {
        return null
    }

    /**
     * 조건문 연산자 체크 진행
     */
    private fun conditionOperatorCheck(condition: String): String? {
        return null
    }

    /**
     * 조건문 내부 태그 대괄호('[]') 체크
     */
    private fun conditionTagBracketsCheck(condition: String): String? {
        return null
    }


}