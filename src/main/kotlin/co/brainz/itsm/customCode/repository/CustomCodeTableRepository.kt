package co.brainz.itsm.customCode.repository

import co.brainz.itsm.customCode.entity.CustomCodeTableEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomCodeTableRepository : JpaRepository<CustomCodeTableEntity, String> {
    fun findByOrderByCustomCodeTableNameAsc(): List<CustomCodeTableEntity>
    fun findByCustomCodeTable(customCodeTable: String): CustomCodeTableEntity
}
