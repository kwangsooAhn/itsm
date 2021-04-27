/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api

import co.brainz.api.dto.SearchDto
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import javax.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

open class ApiUtil {

    /**
     * Request To Param
     */
    protected fun setRequestToParam(request: HttpServletRequest): LinkedHashMap<String, Any> {
        val params = LinkedHashMap<String, Any>()
        if (request.getParameter("search") != null) {
            params["search"] = request.getParameter("search")
        }
        if (request.getParameter("limit") != null) {
            params["limit"] = request.getParameter("limit").toLong()
        }
        if (request.getParameter("offset") != null) {
            params["offset"] = request.getParameter("offset").toLong()
        }
        if (request.getParameter("tags") != null) {
            params["tags"] = request.getParameter("tags")
        }
        return params
    }

    /**
     * 일반적인 검색 조건 설정 (공통)
     */
    protected fun setSearchParam(searchDto: SearchDto): LinkedHashMap<String, Any> {
        val params = LinkedHashMap<String, Any>()
        if (searchDto.search != null) {
            params["search"] = searchDto.search
        }
        if (searchDto.limit != null) {
            params["limit"] = searchDto.limit
        }
        if (searchDto.offset != null) {
            params["offset"] = searchDto.offset
        }
        return params
    }

    /**
     * 응답 결과 처리
     */
    protected fun responseValue(request: HttpServletRequest, contents: Any?): ResponseEntity<Any> {
        var value: Any? = contents
        val headers = HttpHeaders()
        var contentType = MediaType.APPLICATION_JSON_UTF8
        headers.date = System.currentTimeMillis()
        when (request.contentType) {
            MediaType.APPLICATION_XML_VALUE -> {
                contentType = MediaType.APPLICATION_XML
                val xmlMapper = XmlMapper()
                value = xmlMapper.writeValueAsString(contents)
            }
        }
        headers.contentType = contentType
        return ResponseEntity(value, headers, HttpStatus.OK)
    }
}
