package co.brainz.itsm.chart.constants

object ChartConditionConstants {
    enum class Arithmetic(val operator: Char) {
        ADDITION('+'),
        SUBTRACTION('-'),
        DIVISION('/'),
        MULTIPLICATION('*')
    }

    enum class Logical(val operator: Char) {

    }

    enum class Comparison(val operator: Char) {

    }

    enum class parentheses(val value: Char) {
        prefixParentheses('('),
        suffixParentheses(')'),
        prefixSquareBrackets('['),
        suffixSquareBrackets(']')
    }
}