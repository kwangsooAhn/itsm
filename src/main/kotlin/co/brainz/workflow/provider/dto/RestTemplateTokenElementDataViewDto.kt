package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateTokenElementDataViewDto(
    var tokenId: String = "",
    var elementId: String = "",
    var attributeData: MutableList<LinkedHashMap<String, String>> = mutableListOf()
) : Serializable