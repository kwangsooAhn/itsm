package co.brainz.itsm.role.specification

import co.brainz.framework.auth.entity.AliceRoleEntity
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

/**
 * 커스텀 코드에서 조건 검색 시 사용.
 */
class RoleCustomCodeSpecification(private val condition: MutableMap<String, Any>?) : Specification<AliceRoleEntity> {

    override fun toPredicate(
        root: Root<AliceRoleEntity>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        if (condition == null) return null
        val predicate = mutableListOf<Predicate>()
        condition.forEach {
            predicate.add(
                criteriaBuilder.equal(
                    root.get<String>(it.key),
                    it.value
                )
            )
        }
        return criteriaBuilder.and(*predicate.toTypedArray())
    }
}