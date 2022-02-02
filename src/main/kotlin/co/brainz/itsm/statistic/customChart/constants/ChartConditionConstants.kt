package co.brainz.itsm.statistic.customChart.constants

object ChartConditionConstants {
    enum class Identifier(val value: String) {
        STRING("string"),
        LONG("long"),
        TAG("tag"),
        LOGICAL("logical"),
        ARITHMETIC("arithmetic"),
        COMPARISON("comparison"),
        PARENTHESES("parentheses"),
        BOOLEAN("boolean")
    }

    enum class Arithmetic(val operator: String) {
        ADDITION("+"),
        SUBTRACTION("-"),
        DIVISION("/"),
        MULTIPLICATION("*")
    }

    enum class Logical(val operator: String) {
        AND("&&"),
        OR("||")
    }

    enum class Comparison(val operator: String) {
        EQUAL("=="),
        NOTEQUAL("!="),
        GREATER_THAN(">"),
        LESS_THAN("<"),
        GREATER_THAN_OR_EQUAL(">="),
        LESS_THAN_OR_EQUAL("<=")


    }

    enum class Parentheses(val value: String) {
        PREFIX_PARENTHESES("("),
        SUFFIX_PARENTHESES(")"),
        PREFIX_SQUARE_BRACKETS("["),
        SUFFIX_SQUARE_BRACKETS("]")
    }
}