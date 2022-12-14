/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.notice.dto.NoticeListDto
import co.brainz.itsm.notice.dto.NoticeSearchCondition
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.entity.QNoticeEntity
import co.brainz.itsm.portal.dto.PortalTopDto
import com.querydsl.core.BooleanBuilder
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

    override fun findNoticeSearch(noticeSearchCondition: NoticeSearchCondition): PagingReturnDto {
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
            .where(builder(noticeSearchCondition, notice))
            .orderBy(notice.createDt.desc())

        if (noticeSearchCondition.isPaging) {
            query.limit(noticeSearchCondition.contentNumPerPage)
            query.offset((noticeSearchCondition.pageNum - 1) * noticeSearchCondition.contentNumPerPage)
        }

        val countQuery = from(notice)
            .select(notice.count())
            .where(builder(noticeSearchCondition, notice))

        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
    }

    override fun findTopNotice(): MutableList<NoticeListDto> {
        val notice = QNoticeEntity.noticeEntity
        val user = QAliceUserEntity.aliceUserEntity
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
            .innerJoin(notice.createUser, user)
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

    private fun builder(noticeSearchCondition: NoticeSearchCondition, notice: QNoticeEntity): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(
            super.likeIgnoreCase(notice.noticeTitle, noticeSearchCondition.searchValue)?.or(
                super.likeIgnoreCase(notice.createUser.userName, noticeSearchCondition.searchValue)
            )
        )
        builder.and(notice.createDt.goe(noticeSearchCondition.formattedFromDt))
        builder.and(notice.createDt.lt(noticeSearchCondition.formattedToDt))

        return builder
    }
}
