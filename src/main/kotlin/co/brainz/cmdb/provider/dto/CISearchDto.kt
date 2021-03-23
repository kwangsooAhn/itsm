package co.brainz.cmdb.provider.dto

import java.io.Serializable

data class CISearchDto(
    val search: String? = null,
    val offset: Long?,
    val limit: Long?,
    val tags: List<String> = emptyList(),
    val flag: String? = ""
) : Serializable
