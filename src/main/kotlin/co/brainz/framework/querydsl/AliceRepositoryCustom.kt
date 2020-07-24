package co.brainz.framework.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.StringPath

interface AliceRepositoryCustom {

    /**
     * like 쿼리
     */
    fun likeIgnoreCase(entityValue: StringPath, input: String?): BooleanExpression? {
        val filteredInput = this.filtered(input)
        return if (filteredInput == null) {
            null
        } else {
            entityValue.likeIgnoreCase("%$filteredInput%", '\\')
        }
    }

    /**
     * equal 쿼리
     */
    fun eq(entityValue: StringPath, input: String?): BooleanExpression? {
        return if (input == null || input.isBlank()) {
            null
        } else {
            entityValue.eq(input)
        }
    }

    /**
     * in 쿼리
     */
    fun inner(entityValue: StringPath, input: List<String>?): BooleanExpression? {
        return if (input == null || input.isEmpty()) {
            null
        } else {
            entityValue.`in`(input)
        }
    }

    /**
     * 쿼리에 사용할 수 있도록 문자열을 치환.
     *
     * escape 문자는 \ 사용.
     */
    private fun filtered(input: String?): String? {
        if (input == null || input.isBlank()) {
            return null
        }

        val regexChar = "[_%\\\\]".toRegex()
        var convert = input
        val findSpecialChar = regexChar.findAll(convert)
        findSpecialChar.forEach {
            convert = convert!!.replace(it.value, "\\" + it.value)
        }
        return convert
    }
}
