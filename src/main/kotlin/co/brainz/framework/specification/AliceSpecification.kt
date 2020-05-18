package co.brainz.framework.specification

import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Predicate

/**
 * Specification 을 위한 공통 클래스
 *
 */
interface AliceSpecification<T> : Specification<T> {

    /**
     * 특수문자를 sql에 사용할 수 있도록 치환.
     */
    private fun convertSpecialChar(text: String): String {
        val regexChar = "[{}\\[\\]/?.,;:|)*~`!^\\-_+<>@#\$%&\\\\=('\"]".toRegex()
        var convert = text
        val findSpecialChar = regexChar.findAll(convert)
        findSpecialChar.forEach {
            convert = convert.replace(it.value, "\\" + it.value)
        }
        return convert
    }

    /**
     * like 검색용 predicate 를 리턴.
     */
    fun like(
        criteriaBuilder: CriteriaBuilder,
        tableColumn: Expression<String>,
        text: String
    ): Predicate {
        val convert = this.convertSpecialChar(text)
        return criteriaBuilder.like(
            criteriaBuilder.lower(tableColumn),
            criteriaBuilder.lower(criteriaBuilder.literal("%$convert%")),
            '\\'
        )
    }
}
