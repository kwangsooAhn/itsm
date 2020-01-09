package co.brainz.itsm.user.entity

import co.brainz.itsm.user.constants.UserConstants
import co.brainz.itsm.user.dto.UserSearchDto
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

/**
 * 사용자 전체 검색시 조회시 사용할 필드를 정의한다
 */
class UserSpecification(private val userSearchDto: UserSearchDto) : Specification<UserEntity> {

    override fun toPredicate(root: Root<UserEntity>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder): Predicate? {
        val searchValue = userSearchDto.searchValue.trim()
        if (userSearchDto.searchKey.size == 0 || searchValue == "") return null
        val predicate = mutableListOf<Predicate>()
        userSearchDto.searchKey.forEach {
            val tableColumn = UserConstants.UserCodeAndColumnMap.getUserCodeToColum(it)
            predicate.add(criteriaBuilder.like(root.get<String>(tableColumn), "%$searchValue%"))
        }
        return criteriaBuilder.or(*predicate.toTypedArray())
    }
}
