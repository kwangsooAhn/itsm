package co.brainz.framework.auth.dto

import java.io.Serializable

data class AliceIpVerificationDto(
    var ipAddr: String = "",
    var ipExplain: String? = null,
    var createUser: String? = null,
    var createDt: String? = null,
    var updateUser: String? = null,
    var updateDt: String? = null
) : Serializable
