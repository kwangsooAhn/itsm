package co.brainz.itsm.notice.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.notice.dto.NoticeListDto
import co.brainz.itsm.notice.entity.NoticeEntity
import java.time.LocalDateTime

interface NoticeRepositoryCustom : AliceRepositoryCustom {

    fun findNoticeTopList(limit: Long): List<NoticeEntity>

    fun findNoticeSearch(searchValue: String, fromDt: LocalDateTime, toDt: LocalDateTime, offset: Long):
            MutableList<NoticeListDto>
}
