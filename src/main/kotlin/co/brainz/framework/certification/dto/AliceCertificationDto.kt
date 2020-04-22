package co.brainz.framework.certification.dto

import java.io.Serializable

data class AliceCertificationDto(
    val userId: String,
    val email: String,
    val certificationCode: String,
    val status: String,
    val password: String? = ""
) : Serializable
