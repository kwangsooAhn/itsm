package co.brainz.itsm.notice.repository

import co.brainz.itsm.notice.entity.NoticeEntity

interface NoticeRepositoryCustom {

    fun findNoticeTopList(limit: Long): List<NoticeEntity>
}
