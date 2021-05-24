package co.brainz.itsm.customCode.dto

import java.io.Serializable

data class CustomCodeListReturnDto(
    val data: MutableList<CustomCodeListDto>,
    val totalCount: Long = 0
) : Serializable
