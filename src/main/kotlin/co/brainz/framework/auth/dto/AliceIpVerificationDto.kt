package co.brainz.framework.auth.dto

import java.io.Serializable

data class AliceIpVerificationDto(
    var ipAddr: String = "",
    var ipExplain: String? = null
) : Serializable
