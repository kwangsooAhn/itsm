package co.brainz.workflow.engine.document.repository.specification

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
     * like 검색시 특수문자를 치환한다.
     */
    fun like(
        criteriaBuilder: CriteriaBuilder,
        tableColumn: Expression<String>,
        likeText: String
    ): Predicate {
        val specialChar = "[{}\\[\\]/?.,;:|)*~`!^\\-_+<>@#\$%&\\\\=('\"]".toRegex()
        var convertSearchText = likeText
        val findSpecialChar = specialChar.findAll(convertSearchText)
        findSpecialChar.forEach {
            convertSearchText = convertSearchText.replace(it.value, "\\" + it.value)
        }

        return criteriaBuilder.like(
            criteriaBuilder.lower(tableColumn),
            criteriaBuilder.lower(criteriaBuilder.literal("%$convertSearchText%")),
            '\\'
        )
    }
}
