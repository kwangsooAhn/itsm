package co.brainz.itsm.portal.mapper

import co.brainz.itsm.download.entity.DownloadEntity
import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.portal.dto.PortalDto
import org.mapstruct.Mapper

@Mapper
interface PortalMapper {
    fun toPortalNoticeListDto(noticeEntity: NoticeEntity): PortalDto

    fun toPortalFaqListDto(faqEntity: FaqEntity): PortalDto

    fun toPortalDownloadListDto(downloadEntity: DownloadEntity): PortalDto
}