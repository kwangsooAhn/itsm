/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.notice.dto.NoticeListDto
import co.brainz.itsm.notice.dto.NoticeSearchCondition
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.portal.dto.PortalTopDto
import com.querydsl.core.QueryResults

interface NoticeRepositoryCustom : AliceRepositoryCustom {

    fun findNoticeTopList(limit: Long): List<PortalTopDto>

    fun findNoticeSearch(noticeSearchCondition: NoticeSearchCondition): QueryResults<NoticeListDto>

    fun findTopNotice(): MutableList<NoticeListDto>

    fun findNotice(noticeNo: String): NoticeEntity
}
