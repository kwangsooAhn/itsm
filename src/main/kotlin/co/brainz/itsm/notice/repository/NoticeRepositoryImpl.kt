package co.brainz.itsm.notice.repository

import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.entity.QNoticeEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NoticeRepositoryImpl : QuerydslRepositorySupport(NoticeEntity::class.java), NoticeRepositoryCustom {

    override fun findNoticeTopList(limit: Long): List<NoticeEntity> {
        val notice = QNoticeEntity.noticeEntity

        return from(notice).distinct()
            .leftJoin(notice.aliceUserEntity)
            .orderBy(notice.createDt.desc())
            .limit(limit)
            .fetch()
    }
}
