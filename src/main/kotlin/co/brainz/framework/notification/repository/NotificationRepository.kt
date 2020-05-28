package co.brainz.framework.notification.repository

import co.brainz.framework.notification.entity.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<NotificationEntity, String>, NotificationRepositoryCustom