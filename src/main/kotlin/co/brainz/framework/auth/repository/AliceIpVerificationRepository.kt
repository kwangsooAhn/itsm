package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceIpVerificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceIpVerificationRepository: JpaRepository<AliceIpVerificationEntity, String> {
}