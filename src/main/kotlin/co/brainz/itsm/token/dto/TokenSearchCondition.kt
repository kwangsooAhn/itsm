/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.token.dto

import co.brainz.framework.constants.PagingConstants
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.Serializable

/**
 * 문서함 목록 검색 DTO
 */
data class TokenSearchCondition(
    var userKey: String = "",
    var searchTokenType: String = "",
    var searchDocumentId: String = "",
    var searchValue: String = "",
    var searchFromDt: String = "",
    var searchToDt: String = "",
    var documentGroup: String = "",
    var searchTag: String = "",
    val pageNum: Long = 0L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable {
    val isPaging = pageNum > 0
    val tagArray: List<String> = this.tagStrArray()

    private fun tagStrArray(): List<String> {
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val listLinkedMapType: CollectionType =
            TypeFactory.defaultInstance().constructCollectionType(
                List::class.java,
                TypeFactory.defaultInstance().constructMapType(
                    LinkedHashMap::class.java,
                    String::class.java,
                    Any::class.java
                )
            )
        val tagArray = mutableListOf<String>()
        if (!searchTag.isNullOrEmpty()) {
            val tags: List<Map<String, Any>> = mapper.readValue(searchTag.toLowerCase(), listLinkedMapType)
            tags.forEach {
                tagArray.add(it["value"] as String)
            }
        }
        return tagArray.toList()
    }
}
