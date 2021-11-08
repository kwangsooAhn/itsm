package co.brainz.itsm.document.dto

import co.brainz.framework.constants.PagingConstants
import java.io.Serializable

/**
 * 신청서를 검색할 때 사용하는 DTO 클래스
 */
data class DocumentSearchCondition(
    val searchGroupName: String? = "",
    val searchDocuments: String? = "",
    val searchDocumentType: String? = "",
    val searchDocumentStatus: String? = "",
    val searchProcessName: String? = "",
    val searchFormName: String? = "",
    var viewType: String? = "",
    val pageNum: Long = 0L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable {
    val isPaging = pageNum > 0
}
