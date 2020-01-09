package co.brainz.itsm.certification.repository

import co.brainz.itsm.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface CertificationRepository: JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    @Modifying
    @Query("UPDATE UserEntity u SET u.certificationCode = :certificationCode, u.status = :status where u.userId = :userId")
    fun saveCertification(userId: String, certificationCode: String, status: String)

    fun findByUserId(userId: String): UserEntity

    fun countByEmail(email: String): Int
}
