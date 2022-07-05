package co.brainz.itsm.resource.dto

import co.brainz.framework.querydsl.QuerydslConstants
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.resource.ResourceConstants
import java.io.Serializable
import java.time.LocalDateTime

data class ResourceSearchDto(
    val searchValue: String? = null,
    val pageType: String? = ResourceConstants.PageType.THUMBNAIL.code,
    val pageNum: Long = 0L,
    val orderColName: String? = null,
    val orderDir: String? = QuerydslConstants.OrderSpecifier.ASC.code,
    val contentNumPerPage: Long = ItsmConstants.IMAGE_OFFSET_COUNT
) : Serializable {
    val isPaging = pageNum > 0
}

data class ResourcesPagingDto(
    val data: List<ResourceDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable

data class ResourceDto(
    val name: String,
    val extension: String,
    val fullPath: String,
    val isDirectory: Boolean = false,
    val size: String
) : Serializable
