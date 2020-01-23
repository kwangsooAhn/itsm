package co.brainz.itsm.process.dto

data class ProcessDto(
        val processName: String,
        val processDesc: String? = null,
        val formId: String? = null
)