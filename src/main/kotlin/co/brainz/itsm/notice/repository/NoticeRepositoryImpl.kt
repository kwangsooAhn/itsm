package co.brainz.itsm.notice.repository

import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.notice.dto.NoticeListDto
import co.brainz.itsm.notice.dto.NoticeQueryResultDto
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.entity.QNoticeEntity
import com.querydsl.core.types.Projections
import java.time.LocalDateTime
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

    override fun findNoticeSearch(
        searchValue: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): MutableList<NoticeListDto> {
        val notice = QNoticeEntity.noticeEntity
        val query = from(notice)
            .select(
                Projections.constructor(
                    NoticeQueryResultDto::class.java,
                    notice.topNoticeYn,
                    notice.noticeNo,
                    notice.noticeTitle,
                    notice.popYn,
                    notice.popWidth,
                    notice.popHeight,
                    notice.createDt,
                    notice.popStrtDt,
                    notice.popEndDt,
                    notice.topNoticeStrtDt,
                    notice.topNoticeEndDt,
                    notice.aliceUserEntity
                )
            )
            .where(
                super.likeIgnoreCase(notice.noticeTitle, searchValue)?.or(
                    super.likeIgnoreCase(notice.aliceUserEntity.userName, searchValue)
                ),
                notice.createDt.goe(fromDt), notice.createDt.lt(toDt)
            )
            .orderBy(notice.createDt.desc())
            .limit(ItsmConstants.SEARCH_DATA_COUNT)
            .offset(offset)
            .fetchResults()

        val noticeList = mutableListOf<NoticeListDto>()
        for (data in query.results) {
            val noticeListDto = NoticeListDto(
                topNoticeYn = data.topNoticeYn,
                noticeNo = data.noticeNo,
                noticeTitle = data.noticeTitle,
                popYn = data.popYn,
                createDt = data.createDt,
                popStrtDt = data.popStrtDt,
                popEndDt = data.popEndDt,
                topNoticeStrtDt = data.topNoticeStrtDt,
                topNoticeEndDt = data.topNoticeEndDt,
                totalCount = query.total,
                createUserName = data.aliceUserEntity?.userName
            )
            noticeList.add(noticeListDto)
        }
        return noticeList
    }

    override fun findTopNoticeSearch(
        searchValue: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime
    ): MutableList<NoticeListDto> {
        val notice = QNoticeEntity.noticeEntity
        val query = from(notice)
            .select(
                Projections.constructor(
                    NoticeQueryResultDto::class.java,
                    notice.topNoticeYn,
                    notice.noticeNo,
                    notice.noticeTitle,
                    notice.popYn,
                    notice.popWidth,
                    notice.popHeight,
                    notice.createDt,
                    notice.popStrtDt,
                    notice.popEndDt,
                    notice.topNoticeStrtDt,
                    notice.topNoticeEndDt,
                    notice.aliceUserEntity
                )
            )
            .where(
                super.likeIgnoreCase(notice.noticeTitle, searchValue)?.or(
                    super.likeIgnoreCase(notice.aliceUserEntity.userName, searchValue)
                ),
                notice.createDt.goe(fromDt), notice.createDt.lt(toDt), notice.topNoticeYn.eq(true)
            )
            .orderBy(notice.createDt.desc())
            .limit(ItsmConstants.SEARCH_DATA_COUNT)
            .fetch()

        val noticeList = mutableListOf<NoticeListDto>()
        for (data in query) {
            val noticeListDto = NoticeListDto(
                topNoticeYn = data.topNoticeYn,
                noticeNo = data.noticeNo,
                noticeTitle = data.noticeTitle,
                popYn = data.popYn,
                createDt = data.createDt,
                popStrtDt = data.popStrtDt,
                popEndDt = data.popEndDt,
                topNoticeStrtDt = data.topNoticeStrtDt,
                topNoticeEndDt = data.topNoticeEndDt,
                createUserName = data.aliceUserEntity?.userName
            )
            noticeList.add(noticeListDto)
        }
        return noticeList
    }
}
