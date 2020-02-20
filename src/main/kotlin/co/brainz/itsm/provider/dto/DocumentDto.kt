package co.brainz.itsm.provider.dto

import java.io.Serializable

data class DocumentDto(
        val documentId: String,
        val documentName: String,
        val documentDesc: String? = null
) : Serializable
