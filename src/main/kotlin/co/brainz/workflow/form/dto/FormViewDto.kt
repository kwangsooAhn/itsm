package co.brainz.workflow.form.dto

import java.io.Serializable

data class FormViewDto(
        val id: String,
        val name: String,
        val desc: String?
): Serializable

