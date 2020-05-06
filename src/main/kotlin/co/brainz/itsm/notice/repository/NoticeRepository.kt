package co.brainz.itsm.notice.repository

import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.portal.dto.PortalDto
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface NoticeRepository : JpaRepository<NoticeEntity, String> {

    @Query("select a from NoticeEntity a join fetch a.createUser left outer join a.updateUser where (a.topNoticeStrtDt < now() and a.topNoticeEndDt > now() and a.topNoticeYn = true) order by a.createDt desc")
    fun findTopNoticeList(): MutableList<NoticeEntity>

    @Query("select a from NoticeEntity a join fetch a.createUser left outer join a.updateUser where (a.topNoticeStrtDt < now() and a.topNoticeEndDt > now() and a.topNoticeYn = true) and (lower(a.noticeTitle) like lower(concat('%', :searchValue, '%')) or lower(a.aliceUserEntity.userName) like lower(concat('%', :searchValue, '%'))) order by a.createDt desc")
    fun findTopNoticeSearch(searchValue: String): MutableList<NoticeEntity>

    @Query("select a from NoticeEntity a join fetch a.createUser left outer join a.updateUser")
    fun findAllByOrderByCreateDtDesc(): MutableList<NoticeEntity>

    @Query("select a from NoticeEntity a join fetch a.createUser left outer join a.updateUser where (lower(a.noticeTitle) like lower(concat('%', :searchValue, '%')) or lower(a.aliceUserEntity.userName) like lower(concat('%', :searchValue, '%'))) and a.createDt between :fromDt and :toDt order by a.createDt desc")
    fun findNoticeSearch(searchValue: String, fromDt: LocalDateTime, toDt: LocalDateTime): MutableList<NoticeEntity>

    @Query("select a from NoticeEntity a join fetch a.createUser left outer join a.updateUser where (a.popStrtDt < now() and a.popEndDt > now()) and a.popYn = true")
    fun findNoticePopUp(): MutableList<NoticeEntity>

    fun findByNoticeNo(noticeNo: String): NoticeEntity

    @Query(name = "portalSearchMapping",
           nativeQuery = true
    )
    fun findPortalListOrSearchList(searchValue: String, pageable: Pageable?): MutableList<PortalDto>
}