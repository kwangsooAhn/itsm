package co.brainz.itsm.portal.repository

import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.download.entity.QDownloadEntity
import co.brainz.itsm.faq.entity.QFaqEntity
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.entity.QNoticeEntity
import co.brainz.itsm.portal.dto.PortalDto
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class PortalRepositoryImpl : QuerydslRepositorySupport(NoticeEntity::class.java), PortalRepositoryCustom {

    override fun findPortalSearchList(searchValue: String, offset: Long): MutableList<PortalDto> {
        val notice = QNoticeEntity.noticeEntity
        val faq = QFaqEntity.faqEntity
        val download = QDownloadEntity.downloadEntity

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
                        Expressions.asString("notice"),
                        Expressions.asNumber(0)
                    )
                )
                .where(notice.noticeTitle.containsIgnoreCase(searchValue))
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
                        Expressions.asString("faq"),
                        Expressions.asNumber(0)
                    )
                )
                .where(faq.faqTitle.containsIgnoreCase(searchValue))
                .fetch()

        val downloadList =
            from(download)
                .select(
                    Projections.constructor(
                        PortalDto::class.java,
                        download.downloadId,
                        download.downloadTitle,
                        download.downloadCategory,
                        download.createDt,
                        download.updateDt,
                        Expressions.asString("download"),
                        Expressions.asNumber(0)
                    )
                )
                .where(download.downloadTitle.containsIgnoreCase(searchValue))
                .fetch()

        var list = mutableListOf<PortalDto>()
        list.addAll(noticeList)
        list.addAll(faqList)
        list.addAll(downloadList)

        val totalSize = list.size
        if (!list.isNullOrEmpty()) {
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

        list.forEach { data ->
            data.totalCount = totalSize
        }

        return list
    }
}
