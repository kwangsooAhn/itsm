/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.response.dto

import co.brainz.framework.response.ZResponseConstants
import java.io.Serializable

data class ZResponse(
    val status: String = ZResponseConstants.STATUS.SUCCESS.code,
    var message: String? = null,
    val data: Any? = null
) : Serializable
