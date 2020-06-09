package co.brainz.workflow.provider.dto

import java.io.Serializable

/**
 * 문서함 목록 검색 DTO
 */
data class RestTemplateTokenSearchListDto(
    var searchTokenType: String,
    var searchDocumentId: String,
    var searchValue: String,
    var searchFromDt: String,
    var searchToDt: String,
    var offset: String
) : Serializable
