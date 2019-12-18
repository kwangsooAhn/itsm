package co.brainz.itsm.certification

import java.io.Serializable

data class CertificationDto(
        val userId: String,
        val email: String,
        val certificationCode: String,
        val status: String
): Serializable