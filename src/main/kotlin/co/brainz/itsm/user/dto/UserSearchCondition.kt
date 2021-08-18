package co.brainz.itsm.user.dto

import co.brainz.framework.constants.PagingConstants
import java.io.Serializable

data class UserSearchCondition(
    val searchValue: String? = null,
    val pageNum: Long = 1L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable
