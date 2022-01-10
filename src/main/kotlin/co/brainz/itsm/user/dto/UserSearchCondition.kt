package co.brainz.itsm.user.dto

import co.brainz.framework.constants.PagingConstants
import java.io.Serializable

data class UserSearchCondition(
    val searchValue: String? = null,
    val pageNum: Long = 0L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE,
    val isFilterUseYn: Boolean = false
) : Serializable {
    val isPaging = pageNum > 0
    var excludeIds: Set<String> = emptySet()
}
