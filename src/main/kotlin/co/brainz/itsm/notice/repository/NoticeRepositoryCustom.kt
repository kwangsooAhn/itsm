/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.notice.dto.NoticeListDto
import co.brainz.itsm.notice.dto.NoticeListReturnDto
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.portal.dto.PortalTopDto
import java.time.LocalDateTime

interface NoticeRepositoryCustom : AliceRepositoryCustom {

    fun findNoticeTopList(limit: Long): List<PortalTopDto>

    fun findNoticeSearch(
        searchValue: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long,
        limit: Long
    ): NoticeListReturnDto

    fun findTopNotice(fromDt: LocalDateTime, toDt: LocalDateTime): MutableList<NoticeListDto>

    fun findNotice(noticeNo: String): NoticeEntity
}
