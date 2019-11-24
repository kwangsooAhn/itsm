package co.brainz.itsm.notice.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import co.brainz.itsm.notice.domain.Notice
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.time.LocalDateTime


@Repository
public interface NoticeRepository : JpaRepository<Notice, String> {

	@Query("select a from Notice a where (a.topNoticeStrtDt < now() and a.topNoticeEndDt > now()) and a.topNoticeYn = true")
	fun findTopNoticeList(): MutableList<Notice>

	@Query("select a from Notice a where a.noticeTitle like %?1% and a.createDt between ?2 and ?3")
	fun findAllByTitle(keyword: String, fromDate: LocalDateTime, toDate: LocalDateTime): MutableList<Notice>

	@Query("select a from Notice a where a.createUserid like %?1% and a.createDt between ?2 and ?3" )
	fun findAllByWriter(keyword: String, fromDate: LocalDateTime, toDate: LocalDateTime): MutableList<Notice>

	@Query("select a from Notice a where ( a.noticeTitle like %?1% or a.createUserid like %?2% ) and a.createDt between ?3 and ?4")
	fun findAllCheck(keyword: String, copyKeyword: String, fromDate: LocalDateTime, toDate: LocalDateTime): MutableList<Notice>
}