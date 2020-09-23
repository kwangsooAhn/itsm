/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.repository

import co.brainz.itsm.notice.entity.NoticeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface NoticeRepository : JpaRepository<NoticeEntity, String>, NoticeRepositoryCustom {

    @Query(
        "select a from NoticeEntity a join fetch a.createUser " +
                "left outer join a.updateUser where (a.popStrtDt < now() and a.popEndDt > now()) and a.popYn = true"
    )
    fun findNoticePopUp(): MutableList<NoticeEntity>

    fun findByNoticeNo(noticeNo: String): NoticeEntity
}
