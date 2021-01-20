package co.brainz.cmdb.provider.dto

import java.io.Serializable

data class CmdbClassAttributeListDto(
    val attributeId: String? = null,
    val attributeName: String? = null,
    val attributeText: String? = "",
    val attributeType: String? = null,
    val attributeDesc: String? = null,
    var extends: Boolean = false,
    var checkable: Boolean = false
) : Serializable
