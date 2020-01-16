package co.brainz.itsm.form.dto

data class FormDto(
        val formName: String,
        val formDesc: String? = null,
        val lang: String
)