package co.brainz.itsm.notice.entity

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GenerationType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column
import java.time.LocalDateTime
import org.hibernate.annotations.GenericGenerator
import com.fasterxml.jackson.annotation.JsonFormat

@Entity
@Table(name = "portal_notice")
public data class NoticeEntity(
    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid") @Column(name="notice_no") var noticeNo: String? = null,
	@Column(name="notice_title") var noticeTitle: String? = null,
	@Column(name="notice_contents") var noticeContents: String? = null,
	@Column(name="pop_yn") var popYn: Boolean? = null,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Column(name="pop_strt_dt") var popStrtDt: LocalDateTime? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Column(name="pop_end_dt") var popEndDt: LocalDateTime? = null,
	@Column(name="pop_width") var popWidth: Int? = null,
	@Column(name="pop_height") var popHeight: Int? = null,
	@Column(name="top_notice_yn") var topNoticeYn: Boolean? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Column(name="top_notice_strt_dt") var topNoticeStrtDt: LocalDateTime? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Column(name="top_notice_end_dt") var topNoticeEndDt: LocalDateTime? = null,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Column(name="create_dt") var createDt: LocalDateTime? = null,
	@Column(name="create_userid") var createUserid: String? = null,
	@Column(name="update_dt") var updateDt: LocalDateTime? = null,
	@Column(name="update_userid") var updateUserid: String? = null

) : Serializable {
}