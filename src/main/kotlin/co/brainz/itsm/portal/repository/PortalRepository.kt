package co.brainz.itsm.portal.repository

import co.brainz.itsm.notice.entity.NoticeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PortalRepository : JpaRepository<NoticeEntity, String>, PortalRepositoryCustom
