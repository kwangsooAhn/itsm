package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateFormViewDto(
        val id: String = "",
        val name: String = "",
        val desc: String? = null,
        val status: String? = ""
) : Serializable
