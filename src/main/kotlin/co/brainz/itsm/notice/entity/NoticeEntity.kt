/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "portal_notice")
data class NoticeEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "notice_no")
    var noticeNo: String = "",

    @Column(name = "notice_title")
    var noticeTitle: String = "",

    @Column(name = "notice_contents")
    var noticeContents: String = "",

    @Column(name = "pop_yn")
    var popYn: Boolean = true,

    @Column(name = "pop_strt_dt")
    var popStrtDt: LocalDateTime? = null,

    @Column(name = "pop_end_dt")
    var popEndDt: LocalDateTime? = null,

    @Column(name = "pop_width")
    var popWidth: Int? = 500,

    @Column(name = "pop_height")
    var popHeight: Int? = 500,

    @Column(name = "top_notice_yn")
    var topNoticeYn: Boolean = true,

    @Column(name = "top_notice_strt_dt")
    var topNoticeStrtDt: LocalDateTime? = null,

    @Column(name = "top_notice_end_dt")
    var topNoticeEndDt: LocalDateTime? = null
) : Serializable, AliceMetaEntity()
