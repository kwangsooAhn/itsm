package co.brainz.itsm.settings.user

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
        if (userSearchDto.searchKey == "" || userSearchDto.searchValue == "") return null
        val tableColumn = UserConstants.UserCodeAndColumnMap.getUserCodeToColum(userSearchDto.searchKey)
        val predicate = mutableListOf<Predicate>()
        predicate.add(criteriaBuilder.like(root.get<String>(tableColumn), "%" + userSearchDto.searchValue + "%"))
        return criteriaBuilder.and(*predicate.toTypedArray())
    }

}