package co.brainz.itsm.notice.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import co.brainz.itsm.notice.domain.Notice

@Repository
public interface NoticeRepository : JpaRepository<Notice, String> {
}