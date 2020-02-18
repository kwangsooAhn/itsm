package co.brainz.framework.auth.repository

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.notice.entity.NoticeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface AliceUserRepository: JpaRepository<AliceUserEntity, String> {
    fun findByUserId(userId: String): AliceUserEntity

    @Query("select a from NoticeEntity a where (a.topNoticeStrtDt < now() and a.topNoticeEndDt > now() and a.topNoticeYn = true) and (lower(a.noticeTitle) like lower(concat('%', :searchValue, '%')) or lower(a.aliceUserEntity.userName) like lower(concat('%', :searchValue, '%'))) and a.createDt between :fromDt and :toDt order by a.createDt desc")
    fun findByUserInfo(userId: String): AliceUserAuthDto
    fun findByOauthKeyAndPlatform(oauthKey: String, platform: String): AliceUserEntity
}