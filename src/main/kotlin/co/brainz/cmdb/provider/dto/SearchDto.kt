package co.brainz.cmdb.provider.dto

import java.io.Serializable

data class SearchDto(
    val search: String? = null,
    val offset: Long?,
    val limit: Long?
) : Serializable
