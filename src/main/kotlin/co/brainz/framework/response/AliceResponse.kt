/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.response

import co.brainz.framework.response.dto.ResponseDto
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AliceResponse {

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
        fun response(isSuccess: Boolean): ResponseEntity<ResponseDto> {
            val httpStatus = if (isSuccess) HttpStatus.OK else HttpStatus.INTERNAL_SERVER_ERROR
            val response = ResponseDto(
                status = httpStatus.value(),
                message = httpStatus.reasonPhrase
            )
            return ResponseEntity(response, this.setHeader(), httpStatus)
        }

        /**
         * Response [data]
         */
        fun response(data: Any?): ResponseEntity<ResponseDto> {
            val response = ResponseDto(
                data = data
            )
            return ResponseEntity(response, this.setHeader(), HttpStatus.OK)
        }

        /**
         * Response [status], [data]
         */
        fun response(status: HttpStatus, data: Any?): ResponseEntity<ResponseDto> {
            val response = ResponseDto(
                status = status.value(),
                message = status.reasonPhrase,
                data = data
            )
            return ResponseEntity(response, this.setHeader(), status)
        }

        /**
         * Response [status], [message], [data]
         */
        fun response(status: HttpStatus, message: String?, data: Any?): ResponseEntity<ResponseDto> {
            val response = ResponseDto(
                status = status.value(),
                message = if (message.isNullOrBlank()) status.reasonPhrase else message,
                data = data
            )
            return ResponseEntity(response, this.setHeader(), status)
        }

        /**
         * Error Response [body]
         */
        fun responseError(body: ResponseEntity<MutableMap<String, Any>>): ResponseEntity<Map<String, Any?>> {
            val response = LinkedHashMap<String, Any?>()
            response["status"] = body.statusCodeValue
            response["message"] = body.statusCode.reasonPhrase
            response["data"] = null
            return ResponseEntity(response, body.statusCode)
        }
    }
}
