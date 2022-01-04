package co.brainz.itsm.group.dto

import java.io.Serializable
import co.brainz.framework.constants.PagingConstants

data class GroupSearchCondition(
    val searchValue: String? = null,
    val pageNum: Long = 0L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable {
    val isPaging = pageNum > 0
}
