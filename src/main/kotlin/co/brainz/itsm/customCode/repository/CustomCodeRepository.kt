package co.brainz.itsm.customCode.repository

import co.brainz.itsm.customCode.entity.CustomCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomCodeRepository : JpaRepository<CustomCodeEntity, String> {
    fun findByOrderByCustomCodeNameAsc(): List<CustomCodeEntity>
    fun existsByCustomCodeName(customCodeName: String): Boolean
}
