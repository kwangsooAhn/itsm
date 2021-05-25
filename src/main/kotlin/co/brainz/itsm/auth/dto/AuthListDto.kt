/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.auth.dto

import java.io.Serializable

data class AuthListDto(
    val authId: String,
    val authName: String,
    val authDesc: String?
) : Serializable
