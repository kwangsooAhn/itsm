/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.download.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.download.dto.DownloadListDto
import co.brainz.itsm.download.dto.DownloadSearchCondition
import co.brainz.itsm.download.entity.DownloadEntity
import co.brainz.itsm.portal.dto.PortalTopDto
import com.querydsl.core.QueryResults

interface DownloadRepositoryCustom : AliceRepositoryCustom {

    fun findDownloadEntityList(downloadSearchCondition: DownloadSearchCondition): QueryResults<DownloadListDto>

    fun findDownloadTopList(limit: Long): List<PortalTopDto>

    fun findDownload(downloadId: String): DownloadEntity
}
