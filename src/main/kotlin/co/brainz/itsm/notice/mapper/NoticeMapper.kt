package co.brainz.itsm.notice.mapper

import co.brainz.itsm.notice.dto.NoticeDto
import co.brainz.itsm.notice.dto.NoticeListDto
import co.brainz.itsm.notice.dto.NoticePopupDto
import co.brainz.itsm.notice.dto.NoticePopupListDto
import co.brainz.itsm.notice.entity.NoticeEntity
import org.mapstruct.Mapper

@Mapper
interface NoticeMapper {
    fun toNoticePopupDto(noticeEntity: NoticeEntity): NoticePopupDto

    fun toNoticePopupListDto(noticeEntity: NoticeEntity): NoticePopupListDto

    fun toNoticeDto(noticeEntity: NoticeEntity): NoticeDto

    fun toNoticeListDto(noticeEntity: NoticeEntity): NoticeListDto
}