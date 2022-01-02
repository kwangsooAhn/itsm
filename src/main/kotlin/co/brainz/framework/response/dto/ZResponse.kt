/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.response.dto

import java.io.Serializable
import org.springframework.http.HttpStatus

data class ZResponse(
    val status: Int = HttpStatus.OK.value(),
    var message: String? = HttpStatus.OK.reasonPhrase,
    val data: Any? = null
) : Serializable {
}
