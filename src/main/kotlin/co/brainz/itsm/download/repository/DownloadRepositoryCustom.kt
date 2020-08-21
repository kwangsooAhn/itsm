/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.download.repository

import co.brainz.itsm.download.dto.DownloadListDto
import co.brainz.itsm.download.entity.DownloadEntity
import java.time.LocalDateTime

interface DownloadRepositoryCustom {

    fun findDownloadEntityList(
        category: String,
        search: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): List<DownloadListDto>

    fun findDownloadTopList(limit: Long): List<DownloadEntity>
}
