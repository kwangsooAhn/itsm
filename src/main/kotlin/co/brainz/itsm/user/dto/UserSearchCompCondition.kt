package co.brainz.itsm.user.dto

import co.brainz.framework.constants.PagingConstants
import java.io.Serializable

data class UserSearchCompCondition(
    val searchValue: String? = null,
    val targetCriteria: String? = null,
    val searchKeys: String? = null,
    val pageNum: Long = 0L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable {
    val isPaging = pageNum > 0
}
