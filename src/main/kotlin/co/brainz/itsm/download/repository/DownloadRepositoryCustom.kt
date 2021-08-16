/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.download.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.download.dto.DownloadListReturnDto
import co.brainz.itsm.download.dto.DownloadSearchCondition
import co.brainz.itsm.download.entity.DownloadEntity
import co.brainz.itsm.portal.dto.PortalTopDto

interface DownloadRepositoryCustom : AliceRepositoryCustom {

    fun findDownloadEntityList(downloadSearchCondition: DownloadSearchCondition): DownloadListReturnDto

    fun findDownloadTopList(limit: Long): List<PortalTopDto>

    fun findDownload(downloadId: String): DownloadEntity
}
