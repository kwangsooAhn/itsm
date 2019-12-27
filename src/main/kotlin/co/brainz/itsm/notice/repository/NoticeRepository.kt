package co.brainz.itsm.notice.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import co.brainz.itsm.notice.entity.NoticeEntity
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.time.LocalDateTime


@Repository
public interface NoticeRepository : JpaRepository<NoticeEntity, String> {

	@Query("select a from NoticeEntity a where (a.topNoticeStrtDt < now() and a.topNoticeEndDt > now()) and a.topNoticeYn = true")
	fun findTopNoticeList(): MutableList<NoticeEntity>

	@Query("select a from NoticeEntity a where a.noticeTitle like %?1% and a.createDt between ?2 and ?3")
	fun findAllByTitle(keyword: String, fromDt: LocalDateTime, toDt: LocalDateTime): MutableList<NoticeEntity>

	@Query("select a from NoticeEntity a where a.createUserid like %?1% and a.createDt between ?2 and ?3" )
	fun findAllByWriter(keyword: String, fromDt: LocalDateTime, toDt: LocalDateTime): MutableList<NoticeEntity>

	@Query("select a from NoticeEntity a where (a.noticeTitle like %?1% or a.createUserid like %?2% ) and a.createDt between ?3 and ?4")
	fun findAllCheck(keyword: String, copyKeyword: String, fromDt: LocalDateTime, toDt: LocalDateTime): MutableList<NoticeEntity>
	
	@Query("select a from NoticeEntity a where (a.popStrtDt < now() and a.popEndDt > now()) and a.popYn = true")
    fun findNoticePopUp(): MutableList<NoticeEntity>
}