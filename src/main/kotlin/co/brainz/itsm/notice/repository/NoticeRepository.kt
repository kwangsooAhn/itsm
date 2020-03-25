package co.brainz.itsm.notice.repository

import co.brainz.itsm.notice.entity.NoticeEntity
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface NoticeRepository: JpaRepository<NoticeEntity, String> {

    @Query("select a from NoticeEntity a where (a.topNoticeStrtDt < now() and a.topNoticeEndDt > now() and a.topNoticeYn = true) order by a.createDt desc")
    fun findTopNoticeList(): MutableList<NoticeEntity>

    @Query("select a from NoticeEntity a where (a.topNoticeStrtDt < now() and a.topNoticeEndDt > now() and a.topNoticeYn = true) and (lower(a.noticeTitle) like lower(concat('%', :searchValue, '%')) or lower(a.aliceUserEntity.userName) like lower(concat('%', :searchValue, '%'))) order by a.createDt desc")
    fun findTopNoticeSearch(searchValue: String): MutableList<NoticeEntity>

    fun findAllByOrderByCreateDtDesc(): MutableList<NoticeEntity>

    @Query("select a from NoticeEntity a where (lower(a.noticeTitle) like lower(concat('%', :searchValue, '%')) or lower(a.aliceUserEntity.userName) like lower(concat('%', :searchValue, '%'))) and a.createDt between :fromDt and :toDt order by a.createDt desc")
    fun findNoticeSearch(searchValue: String, fromDt: LocalDateTime, toDt: LocalDateTime): MutableList<NoticeEntity>
    
    @Query("select a from NoticeEntity a where (a.popStrtDt < now() and a.popEndDt > now()) and a.popYn = true")
    fun findNoticePopUp(): MutableList<NoticeEntity>

    fun findByNoticeNo(noticeNo: String): NoticeEntity
}