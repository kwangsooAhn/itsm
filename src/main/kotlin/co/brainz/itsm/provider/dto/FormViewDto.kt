package co.brainz.itsm.provider.dto

import java.io.Serializable

data class FormViewDto(
        val id: String = "",
        val name: String = "",
        val desc: String? = null,
        val status: String? = ""
) : Serializable
