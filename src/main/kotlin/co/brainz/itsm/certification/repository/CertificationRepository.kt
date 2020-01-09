package co.brainz.itsm.certification.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface CertificationRepository: JpaRepository<AliceUserEntity, String>, JpaSpecificationExecutor<AliceUserEntity> {

    @Modifying
    @Query("UPDATE AliceUserEntity u SET u.certificationCode = :certificationCode, u.status = :status where u.userId = :userId")
    fun saveCertification(userId: String, certificationCode: String, status: String)

    fun findByUserId(userId: String): AliceUserEntity

    fun countByEmail(email: String): Int
}
