package co.brainz.cmdb.dto

import java.io.Serializable

data class CIGroupListDto(
    val ciId: String = "",
    val attributeId: String = "",
    val cAttributeId: String = "",
    val cAttributeSeq: Int? = 0,
    var cValue: String? = null
) : Serializable
