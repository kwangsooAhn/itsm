/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.notice.dto.NoticeListDto
import co.brainz.itsm.notice.dto.NoticeSearchCondition
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.portal.dto.PortalTopDto

interface NoticeRepositoryCustom : AliceRepositoryCustom {

    fun findNoticeTopList(limit: Long): List<PortalTopDto>

    fun findNoticeSearch(noticeSearchCondition: NoticeSearchCondition): PagingReturnDto

    fun findTopNotice(): MutableList<NoticeListDto>

    fun findNotice(noticeNo: String): NoticeEntity
}
