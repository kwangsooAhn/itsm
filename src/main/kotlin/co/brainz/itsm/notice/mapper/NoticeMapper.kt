package co.brainz.itsm.notice.mapper

import co.brainz.itsm.notice.dto.NoticeDto
import co.brainz.itsm.notice.dto.NoticeListDto
import co.brainz.itsm.notice.dto.NoticePopupDto
import co.brainz.itsm.notice.dto.NoticePopupListDto
import co.brainz.itsm.notice.entity.NoticeEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface NoticeMapper {
    @Mappings(
        Mapping(target = "createUserKey", ignore = true)
    )
    fun toNoticePopupDto(noticeEntity: NoticeEntity): NoticePopupDto

    fun toNoticePopupListDto(noticeEntity: NoticeEntity): NoticePopupListDto

    fun toNoticeDto(noticeEntity: NoticeEntity): NoticeDto

    fun toNoticeListDto(noticeEntity: NoticeEntity): NoticeListDto
}
