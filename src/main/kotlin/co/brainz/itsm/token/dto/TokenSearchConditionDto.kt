package co.brainz.itsm.token.dto

import java.io.Serializable

/**
 * 문서함 목록 검색 DTO
 */
data class TokenSearchConditionDto(
    var userKey: String = "",
    var searchTokenType: String = "",
    var searchDocumentId: String = "",
    var searchValue: String = "",
    var searchFromDt: String = "",
    var searchToDt: String = "",
    var offset: Long = 100,
    var searchTagString: String = "",
    var searchTagSet: Set<String> = mutableSetOf<String>(),
    var isScroll: Boolean = false
) : Serializable
