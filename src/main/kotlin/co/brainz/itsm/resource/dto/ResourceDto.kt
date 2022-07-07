package co.brainz.itsm.resource.dto

import co.brainz.framework.fileTransaction.constants.ResourceConstants
import co.brainz.framework.querydsl.QuerydslConstants
import co.brainz.framework.util.AlicePagingData
import java.io.Serializable
import java.time.LocalDateTime

data class ResourceSearchDto(
    val type: String = ResourceConstants.FileType.FILE.code,
    val searchPath: String = "",
    val searchValue: String = "",
    val pageType: String = ResourceConstants.PageType.THUMBNAIL.code,
    val pageNum: Long = 0L,
    val orderColName: String? = null,
    val orderDir: String? = QuerydslConstants.OrderSpecifier.ASC.code,
    val contentNumPerPage: Long = ResourceConstants.OffsetCount.THUMBNAIL.value
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
    val directoryYn: Boolean = false,
    val imageFileYn: Boolean = false,
    val size: String,
    val data: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val updateDt: LocalDateTime?,
    val editable: Boolean = true
) : Serializable
