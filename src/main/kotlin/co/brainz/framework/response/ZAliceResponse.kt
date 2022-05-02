/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.response

import co.brainz.framework.response.dto.ZResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity

class ZAliceResponse {

    companion object {

        /**
         * Response Header
         */
        private fun setHeader(): HttpHeaders {
            val headers = HttpHeaders()
            headers.date = System.currentTimeMillis()
            headers.contentType = MediaType.APPLICATION_JSON_UTF8
            return headers
        }

        /**
         * Response [isSuccess]
         */
        fun response(isSuccess: Boolean): ResponseEntity<ZResponse> {
            val httpStatus = HttpStatus.OK
            val response = ZResponse()
            return ResponseEntity(response, this.setHeader(), httpStatus)
        }

        /**
         * Response [response]
         */
        fun response(response: ZResponse): ResponseEntity<ZResponse> {
            return ResponseEntity(response, this.setHeader(), HttpStatus.OK)
        }

        /**
         * Response [data]
         */
        fun response(data: Any?): ResponseEntity<ZResponse> {
            val response = ZResponse(
                data = data
            )
            return ResponseEntity(response, this.setHeader(), HttpStatus.OK)
        }

        /**
         * Error Response [body]
         */
        fun responseError(body: ResponseEntity<MutableMap<String, Any>>): ResponseEntity<Map<String, Any?>> {
            val response = LinkedHashMap<String, Any?>()
            response["status"] = body.body?.get("status") ?: body.statusCode
            response["message"] = body.body?.get("message") ?: body.statusCode.reasonPhrase
            response["data"] = null
            return ResponseEntity(response, body.statusCode)
        }
    }
}
