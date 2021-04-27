package co.brainz.cmdb.dto

import java.io.Serializable

data class CIClassAttributeListDto(
    val attributeId: String? = null,
    val attributeName: String? = null,
    val attributeText: String? = "",
    val attributeType: String? = null,
    val attributeDesc: String? = null,
    var extends: Boolean = false
) : Serializable
