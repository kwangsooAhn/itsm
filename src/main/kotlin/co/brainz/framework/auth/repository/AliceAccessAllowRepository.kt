package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceAccessAllowEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceAccessAllowRepository: JpaRepository<AliceAccessAllowEntity, String> {
}