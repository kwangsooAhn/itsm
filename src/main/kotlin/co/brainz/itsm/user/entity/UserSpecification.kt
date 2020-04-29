package co.brainz.itsm.user.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.code.entity.CodeEntity
import co.brainz.itsm.user.constants.UserConstants
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

/**
 * 사용자 전체 검색시 조회시 사용할 필드를 정의한다
 */
class UserSpecification(private val codeEntity: List<CodeEntity>, private val searchValue: String) :
    Specification<AliceUserEntity> {

    override fun toPredicate(
        root: Root<AliceUserEntity>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        if (searchValue == "") return null
        val predicate = mutableListOf<Predicate>()
        val searchValue = searchValue.replace("_", "\\_").toLowerCase()
        codeEntity.forEach {
            val tableColumn = UserConstants.UserCodeAndColumnMap.getUserCodeToColumn(it.code)
            if (tableColumn != "") {
                predicate.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get<String>(tableColumn)),
                        "%$searchValue%"
                    )
                )
            }
        }
        return criteriaBuilder.or(*predicate.toTypedArray())
    }
}
