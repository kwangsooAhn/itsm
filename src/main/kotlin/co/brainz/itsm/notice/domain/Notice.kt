package co.brainz.itsm.notice.domain

import java.io.Serializable
import javax.persistence.Table
import javax.persistence.Entity
import javax.persistence.GenerationType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Column
import java.time.LocalDateTime
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "portalNoticeTab")
public data class Notice(
    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid") @Column var noticeNo: String? = null,
	@Column var noticeTitle: String? = null,
	@Column var noticeContents: String? = null,
	@Column var popYn: Boolean? = null,
	@Column var popStrtDt: LocalDateTime? = null,
	@Column var popEndDt: LocalDateTime? = null,
	@Column var popWidth: Int? = null,
	@Column var popHeight: Int? = null,
	@Column var topNoticeYn: Boolean? = null,
	@Column var topNoticeStrtDt: LocalDateTime? = null,
	@Column var topNoticeEndDt: LocalDateTime? = null,
	@Column var createDt: LocalDateTime? = null,
	@Column var createUserid: String? = null,
	@Column var updateDt: LocalDateTime? = null,
	@Column var updateUserid: String? = null

) : Serializable {
}