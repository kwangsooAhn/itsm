/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.mapper

import co.brainz.itsm.notice.dto.NoticeDto
import co.brainz.itsm.notice.dto.NoticePopupDto
import co.brainz.itsm.notice.dto.NoticePopupListDto
import co.brainz.itsm.notice.entity.NoticeEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface NoticeMapper {
    @Mappings(
        Mapping(source = "createUser.userName", target = "createUserName")
    )
    fun toNoticePopupDto(noticeEntity: NoticeEntity): NoticePopupDto

    fun toNoticePopupListDto(noticeEntity: NoticeEntity): NoticePopupListDto

    @Mappings(
        Mapping(target = "createUserKey", ignore = true),
        Mapping(target = "updateUserKey", ignore = true),
        Mapping(target = "fileSeq", ignore = true),
        Mapping(source = "createUser.userName", target = "createUserName")
    )
    fun toNoticeDto(noticeEntity: NoticeEntity): NoticeDto
}
