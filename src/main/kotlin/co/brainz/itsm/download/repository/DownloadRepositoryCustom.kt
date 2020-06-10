package co.brainz.itsm.download.repository

import co.brainz.itsm.download.entity.DownloadEntity
import java.time.LocalDateTime

interface DownloadRepositoryCustom {

    fun findDownloadEntityList(
        category: String,
        search: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime
    ): List<DownloadEntity>
}
