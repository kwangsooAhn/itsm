package co.brainz.framework.util;

import java.io.Serializable

data class AlicePagingData(
    var totalCount: Long = 0L,
    var totalCountWithoutCondition: Long = 0L,
    var currentPageNum: Long = 0L,
    var totalPageNum: Long = 0L,
    var orderType: String?
) : Serializable