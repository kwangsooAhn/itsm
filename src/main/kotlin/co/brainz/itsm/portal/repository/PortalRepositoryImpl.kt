/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.repository

import co.brainz.itsm.archive.entity.QArchiveEntity
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.faq.entity.QFaqEntity
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.entity.QNoticeEntity
import co.brainz.itsm.portal.dto.PortalDto
import co.brainz.itsm.portal.dto.PortalListReturnDto
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class PortalRepositoryImpl : QuerydslRepositorySupport(NoticeEntity::class.java), PortalRepositoryCustom {

    override fun findPortalSearchList(searchValue: String, offset: Long): PortalListReturnDto {
        val notice = QNoticeEntity.noticeEntity
        val faq = QFaqEntity.faqEntity
        val archive = QArchiveEntity.archiveEntity
        val code = QCodeEntity.codeEntity

        val noticeList =
            from(notice)
                .select(
                    Projections.constructor(
                        PortalDto::class.java,
                        notice.noticeNo,
                        notice.noticeTitle,
                        notice.noticeContents,
                        notice.createDt,
                        notice.updateDt,
                        Expressions.asString("notice")
                    )
                )
                .where(
                    super.likeIgnoreCase(notice.noticeTitle, searchValue)
                        ?.or(super.likeIgnoreCase(notice.noticeContents, searchValue))
                )
                .fetch()

        val faqList =
            from(faq)
                .select(
                    Projections.constructor(
                        PortalDto::class.java,
                        faq.faqId,
                        faq.faqTitle,
                        faq.faqContent,
                        faq.createDt,
                        faq.updateDt,
                        Expressions.asString("faq")
                    )
                )
                .where(
                    super.likeIgnoreCase(faq.faqTitle, searchValue)
                        ?.or(super.likeIgnoreCase(faq.faqContent, searchValue))
                )
                .fetch()

        val archiveList =
            from(archive)
                .select(
                    Projections.constructor(
                        PortalDto::class.java,
                        archive.archiveId,
                        archive.archiveTitle,
                        code.codeName,
                        archive.createDt,
                        archive.updateDt,
                        Expressions.asString("archive")
                    )
                )
                .innerJoin(code).on(code.code.eq(archive.archiveCategory)).fetchJoin()
                .where(super.likeIgnoreCase(archive.archiveTitle, searchValue))
                .fetch()

        var list = mutableListOf<PortalDto>()
        list.addAll(noticeList)
        list.addAll(faqList)
        list.addAll(archiveList)

        val totalSize = list.size
        if (list.isNotEmpty()) {
            list.sortByDescending { portalDto -> portalDto.createDt }
            var fromRow = offset.toInt()
            var toRow = ItsmConstants.SEARCH_DATA_COUNT.toInt()
            if (fromRow == 0) {
                if (list.size < toRow) {
                    toRow = list.size
                }
            } else {
                if (list.size < fromRow) {
                    fromRow = list.size
                }
                toRow = fromRow + ItsmConstants.SEARCH_DATA_COUNT.toInt()
                if (list.size < toRow) {
                    toRow = list.size
                }
            }
            list = list.subList(fromRow, toRow)
        }

        return PortalListReturnDto(
            data = list,
            totalCount = totalSize.toLong()
        )
    }
}
