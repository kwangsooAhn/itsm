package co.brainz.itsm.notice.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import co.brainz.itsm.notice.domain.Notice
import org.springframework.data.jpa.repository.JpaSpecificationExecutor


@Repository
public interface NoticeRepository : JpaRepository<Notice, String>, JpaSpecificationExecutor<Notice> {

	@Query("select a from Notice a where a.topNoticeYn = true")
	fun findTopNoticeList(): MutableList<Notice>
}