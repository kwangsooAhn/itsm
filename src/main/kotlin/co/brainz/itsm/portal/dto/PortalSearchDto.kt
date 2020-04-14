package co.brainz.itsm.portal.dto

import java.io.Serializable

data class PortalSearchDto(
        val isSearch: Boolean,
        val searchValue: String
) : Serializable