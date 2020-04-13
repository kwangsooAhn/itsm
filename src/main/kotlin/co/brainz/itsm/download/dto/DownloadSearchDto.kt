package co.brainz.itsm.download.dto

import java.io.Serializable

data class DownloadSearchDto(
        var category: String = "",
        var search: String = "",
        var fromDt: String = "",
        var toDt: String = ""
): Serializable