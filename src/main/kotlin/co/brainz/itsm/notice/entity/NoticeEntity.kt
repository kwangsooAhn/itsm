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
@Table(name = "portalNotice")
public data class NoticeEntity(
    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid") @Column var noticeNo: String? = null,
	@Column var noticeTitle: String? = null,
	@Column var noticeContents: String? = null,
	@Column var popYn: Boolean? = null,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Column var popStrtDt: LocalDateTime? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Column var popEndDt: LocalDateTime? = null,
	@Column var popWidth: Int? = null,
	@Column var popHeight: Int? = null,
	@Column var topNoticeYn: Boolean? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Column var topNoticeStrtDt: LocalDateTime? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Column var topNoticeEndDt: LocalDateTime? = null,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@Column var createDt: LocalDateTime? = null,
	@Column var createUserid: String? = null,
	@Column var updateDt: LocalDateTime? = null,
	@Column var updateUserid: String? = null

) : Serializable {
}