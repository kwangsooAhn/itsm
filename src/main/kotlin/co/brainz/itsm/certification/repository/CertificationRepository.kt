package co.brainz.itsm.certification.repository

import co.brainz.itsm.certification.CertificationDto
import co.brainz.itsm.settings.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CertificationRepository: JpaRepository<UserEntity, String> {

    @Modifying
    @Query("UPDATE UserEntity u SET u.certificationCode = :certificationCode, u.status = :status where u.userId = :userId")
    fun saveCertification(userId: String, certificationCode: String, status: String)

    fun findByUserId(userId: String): UserEntity
}