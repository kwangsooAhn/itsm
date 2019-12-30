package co.brainz.itsm.notice.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import co.brainz.itsm.notice.entity.NoticeEntity
import java.time.LocalDateTime


@Repository
interface NoticeRepository: JpaRepository<NoticeEntity, String> {

    @Query("select a from NoticeEntity a where (a.topNoticeStrtDt < now() and a.topNoticeEndDt > now()) and a.topNoticeYn = true order by a.createDt desc")
    fun findTopNoticeList(): MutableList<NoticeEntity>

    @Query("select a from NoticeEntity a where (lower(a.noticeTitle) like lower(concat('%', :keyWord, '%')) or upper(a.noticeTitle) like upper(concat('%', :keyWord, '%'))) and a.createDt between :fromDt and :toDt")
    fun findAllByTitle(keyWord: String, fromDt: LocalDateTime, toDt: LocalDateTime): MutableList<NoticeEntity>

    @Query("select a from NoticeEntity a where (lower(a.createUserid) like lower(concat('%', :keyWord, '%')) or upper(a.createUserid) like upper(concat('%', :keyWord, '%'))) and a.createDt between :fromDt and :toDt" )
    fun findAllByWriter(keyWord: String, fromDt: LocalDateTime, toDt: LocalDateTime): MutableList<NoticeEntity>

    @Query("select a from NoticeEntity a where (lower(a.noticeTitle) like lower(concat('%', :keyWord, '%')) or lower(a.createUserid) like lower(concat('%', :keyWord, '%')) or upper(a.noticeTitle) like upper(concat('%', :keyWord, '%')) or upper(a.createUserid) like lower(concat('%', :keyWord, '%'))) and a.createDt between :fromDt and :toDt")
    fun findAllCheck(keyWord: String, fromDt: LocalDateTime, toDt: LocalDateTime): MutableList<NoticeEntity>
    
    @Query("select a from NoticeEntity a where (a.popStrtDt < now() and a.popEndDt > now()) and a.popYn = true")
    fun findNoticePopUp(): MutableList<NoticeEntity>
}