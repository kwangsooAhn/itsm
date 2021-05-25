package co.brainz.itsm.download.dto

import java.io.Serializable

data class DownloadListReturnDto(
    val data: List<DownloadListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
