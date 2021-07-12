package co.brainz.itsm.user.specification

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.customCode.dto.CustomCodeConditionDto
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

/**
 * 커스텀 코드에서 조건 검색 시 사용.
 */
class UserCustomCodeSpecification(private val condition: Array<CustomCodeConditionDto>?) :
    Specification<AliceUserEntity> {

    override fun toPredicate(
        root: Root<AliceUserEntity>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        if (condition == null) return null
        val predicate = mutableListOf<Predicate>()
        condition.forEach {
            if (it.conditionOperator == "equal") {
                predicate.add(
                    criteriaBuilder.equal(
                        root.get<String>(it.conditionKey),
                        it.conditionValue
                    )
                )
            } else {
                predicate.add(
                    criteriaBuilder.notEqual(
                        root.get<String>(it.conditionKey),
                        it.conditionValue
                    )
                )
            }
        }
        return criteriaBuilder.and(*predicate.toTypedArray())
    }
}
