/**
 * CI Attribute 검색 조건용 데이터 클래스
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */
package co.brainz.itsm.cmdb.ci.dto

import co.brainz.framework.constants.PagingConstants
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.Serializable

/**
 * @param searchValue : 사용자가 입력한 검색어
 * @param pageNum : 검색결과중에서 요청받은 페이지
 * @param contentNumPerPage : 페이지당 출력되는 건수
 */
data class CISearchCondition(
    val searchValue: String = "",
    val tagSearch: String = "",
    val relationSearch: String = "",
    val flag: String = "",
    val pageNum: Long = 0L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable {
    val tagArray: List<String> = this.tagStrArray()
    val isPaging = pageNum > 0

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
        if (!tagSearch.isNullOrEmpty()) {
            val tags: List<Map<String, Any>> = mapper.readValue(tagSearch, listLinkedMapType)
            tags.forEach {
                tagArray.add(it["value"] as String)
            }
        }
        return tagArray.toList()
    }
}
