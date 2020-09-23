package co.brainz.itsm.portal.repository

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

    override fun findPortalSearchList(searchValue: String, limit: Long, offset: Long): MutableList<PortalDto> {
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

        val list = mutableListOf<PortalDto>()
        list.addAll(noticeList)
        list.addAll(faqList)
        list.addAll(downloadList)
        list.sortByDescending { portalDto -> portalDto.createDt }
//        list.subList(offset.toInt(), offset.toInt() + limit.toInt())

        //val list = mutableListOf<PortalDto>()
        //list.addAll(noticeList)
        return list

    }
}
