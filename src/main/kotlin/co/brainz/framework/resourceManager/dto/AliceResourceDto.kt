package co.brainz.framework.resourceManager.dto

import co.brainz.framework.resourceManager.constants.ResourceConstants
import co.brainz.framework.querydsl.QuerydslConstants
import co.brainz.framework.util.AlicePagingData
import java.io.Serializable
import java.time.LocalDateTime

data class AliceResourceDto(
    val name: String,
    val extension: String,
    val fullPath: String,
    val directoryYn: Boolean = false,
    val imageFileYn: Boolean = false,
    val size: String,
    val data: String? = null,
    val width: Int? = null,
    val height: Int? = null,
    val count: Int = 0,
    val updateDt: LocalDateTime?,
    val editable: Boolean = true
): Serializable

data class AliceResourceRenameDto(
    var originPath: String = "",
    var modifyPath: String = ""
)

data class AliceResourceSearchDto(
    val type: String = ResourceConstants.FileType.FILE.code,
    val searchPath: String = "",
    val searchValue: String = "",
    val pageType: String = ResourceConstants.PageType.THUMBNAIL.code,
    val pageNum: Long = 0L,
    val orderColName: String? = null,
    val orderDir: String? = QuerydslConstants.OrderSpecifier.ASC.code
) : Serializable {
    val isPaging = pageNum > 0
}

// 페이징용 데이터 클래스
data class AliceResourcesPagingDto(
    val data: List<AliceResourceDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
