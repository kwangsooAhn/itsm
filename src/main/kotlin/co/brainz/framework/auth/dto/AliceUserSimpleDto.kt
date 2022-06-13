/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.dto

import java.io.Serializable

data class AliceUserSimpleDto(
    val userId: String,
    val password: String
) : Serializable
