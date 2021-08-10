/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.repository

import co.brainz.itsm.notice.dto.NoticeListDto
import co.brainz.itsm.notice.dto.NoticeListReturnDto
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.entity.QNoticeEntity
import co.brainz.itsm.portal.dto.PortalTopDto
import com.querydsl.core.types.Projections
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NoticeRepositoryImpl : QuerydslRepositorySupport(NoticeEntity::class.java), NoticeRepositoryCustom {

    override fun findNoticeTopList(limit: Long): List<PortalTopDto> {
        val notice = QNoticeEntity.noticeEntity

        return from(notice).distinct()
            .select(
                Projections.constructor(
                    PortalTopDto::class.java,
                    notice.noticeNo,
                    notice.noticeTitle,
                    notice.noticeContents,
                    notice.createDt
                )
            )
            .orderBy(notice.createDt.desc())
            .limit(limit)
            .fetch()
    }

    override fun findNoticeSearch(
        searchValue: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long,
        limit: Long
    ): NoticeListReturnDto {
        val notice = QNoticeEntity.noticeEntity
        val query = from(notice)
            .select(
                Projections.constructor(
                    NoticeListDto::class.java,
                    notice.topNoticeYn,
                    notice.noticeNo,
                    notice.noticeTitle,
                    notice.popYn,
                    notice.createDt,
                    notice.popStrtDt,
                    notice.popEndDt,
                    notice.popWidth,
                    notice.popHeight,
                    notice.topNoticeStrtDt,
                    notice.topNoticeEndDt,
                    notice.createUser.userName
                )
            )
            .where(
                super.like(notice.noticeTitle, searchValue)?.or(
                    super.like(notice.createUser.userName, searchValue)
                ),
                notice.createDt.goe(fromDt), notice.createDt.lt(toDt)
            )
            .orderBy(notice.createDt.desc())
            .limit(limit)
            .offset(offset)
            .fetchResults()

        return NoticeListReturnDto(
            data = query.results,
            totalCount = query.total
        )
    }

    override fun findTopNotice(): MutableList<NoticeListDto> {
        val notice = QNoticeEntity.noticeEntity
        val currentDateTime = LocalDateTime.now()
        return from(notice)
            .select(
                Projections.constructor(
                    NoticeListDto::class.java,
                    notice.topNoticeYn,
                    notice.noticeNo,
                    notice.noticeTitle,
                    notice.popYn,
                    notice.createDt,
                    notice.popStrtDt,
                    notice.popEndDt,
                    notice.popWidth,
                    notice.popHeight,
                    notice.topNoticeStrtDt,
                    notice.topNoticeEndDt,
                    notice.createUser.userName
                )
            )
            .where(
                notice.topNoticeStrtDt.loe(currentDateTime),
                notice.topNoticeEndDt.goe(currentDateTime),
                notice.topNoticeYn.eq(true)
            )
            .orderBy(notice.createDt.desc())
            .fetch()
    }

    override fun findNotice(noticeNo: String): NoticeEntity {
        val notice = QNoticeEntity.noticeEntity
        return from(notice)
            .innerJoin(notice.createUser).fetchJoin()
            .leftJoin(notice.updateUser).fetchJoin()
            .where(notice.noticeNo.eq(noticeNo))
            .fetchOne()
    }
}
