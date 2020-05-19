package co.brainz.framework.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.StringPath

interface AliceQueryDsl {

    /**
     * 쿼리에 사용할 수 있도록 문자열을 치환.
     *
     * escape 문자는 \ 사용.
     */
    fun filtered(input: String?): String? {
        if (input == null || input.isBlank()) {
            return null
        }

        val regexChar = "[{}\\[\\]/?.,;:|)*~`!^\\-_+<>@#\$%&\\\\=('\"]".toRegex()
        var convert = input
        val findSpecialChar = regexChar.findAll(convert)
        findSpecialChar.forEach {
            convert = convert!!.replace(it.value, "\\" + it.value)
        }
        return convert
    }

    /**
     * 쿼리에 사용할 수 있도록 like 검색용 치환하여 리턴.
     */
    fun likeIgnoreCase(entityValue: StringPath, input: String?): BooleanExpression? {
        val filteredInput = this.filtered(input)
        return if (filteredInput == null) {
            null
        } else {
            entityValue.likeIgnoreCase(filteredInput, '\\')
        }
    }
}
