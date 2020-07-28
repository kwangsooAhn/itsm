package co.brainz.itsm.faq.dto

import java.io.Serializable

data class FaqSearchRequestDto(
    var search: String? = null,
    var groupCodes: MutableList<String>? = null
) : Serializable
