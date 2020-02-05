package co.brainz.itsm.notice.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auditor.AliceMetaEntity
import co.brainz.itsm.utility.LocalDateTimeAttributeConverter
import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator
import javax.persistence.Convert
import javax.persistence.FetchType

@Entity
@Table(name = "portal_notice")
data class NoticeEntity(
        @Id @GeneratedValue(generator="system-uuid")
        @GenericGenerator(name="system-uuid", strategy = "uuid") @Column(name="notice_no") var noticeNo: String = "",
        @Column(name="notice_title") var noticeTitle: String = "",
        @Column(name="notice_contents") var noticeContents: String = "",
        @Column(name="pop_yn") var popYn: Boolean = true,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        @Convert(converter = LocalDateTimeAttributeConverter::class)
        @Column(name="pop_strt_dt") var popStrtDt: LocalDateTime? = null,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        @Convert(converter = LocalDateTimeAttributeConverter::class)
        @Column(name="pop_end_dt") var popEndDt: LocalDateTime? = null,
        @Column(name="pop_width") var popWidth: Int? = 500,
        @Column(name="pop_height") var popHeight: Int? = 500,
        @Column(name="top_notice_yn") var topNoticeYn: Boolean = true,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        @Convert(converter = LocalDateTimeAttributeConverter::class)
        @Column(name="top_notice_strt_dt") var topNoticeStrtDt: LocalDateTime? = null,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        @Convert(converter = LocalDateTimeAttributeConverter::class)
        @Column(name="top_notice_end_dt") var topNoticeEndDt: LocalDateTime? = null,

        @ManyToOne(targetEntity=AliceUserEntity::class, fetch = FetchType.LAZY)
        @JoinColumn(name = "create_userkey", insertable=false, updatable=false)
        var aliceUserEntity: AliceUserEntity? = null

): Serializable, AliceMetaEntity()
