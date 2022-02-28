package co.brainz.itsm.dashboard.dto

import java.io.Serializable

data class TemplateOrganizationListDto(
    val organizationId: String,
    val organizationName: String,
    val columnTitle: Array<String>,
    val columnWidth: Array<String>,
    val columnType: Array<String>,
    val contents: List<Array<Any>>
) : Serializable
