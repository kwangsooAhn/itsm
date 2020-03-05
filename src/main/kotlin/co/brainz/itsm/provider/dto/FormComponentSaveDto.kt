package co.brainz.itsm.provider.dto

import java.io.Serializable

data class FormComponentSaveDto(
        val form: FormSaveDto,
        var components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
) : Serializable
