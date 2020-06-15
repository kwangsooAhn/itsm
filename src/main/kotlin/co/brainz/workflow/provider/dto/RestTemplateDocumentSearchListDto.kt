package co.brainz.workflow.provider.dto

import java.io.Serializable

/**
 * 신청서를 검색할 때 사용하는 DTO 클래스
 */
data class RestTemplateDocumentSearchListDto(
    var searchGroupName: String? = "",
    var searchDocuments: String? = "",
    var searchDocumentType: String? = "",
    var searchDocumentStatus: String? = "",
    var searchProcessName: String? = "",
    var searchFormName: String? = ""
) : Serializable
