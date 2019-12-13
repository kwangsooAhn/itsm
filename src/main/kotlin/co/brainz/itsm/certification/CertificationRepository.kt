package co.brainz.itsm.certification

import co.brainz.itsm.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface CertificationRepository: JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity>