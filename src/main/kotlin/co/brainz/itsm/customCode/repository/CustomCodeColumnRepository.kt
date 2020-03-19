package co.brainz.itsm.customCode.repository

import co.brainz.itsm.customCode.entity.CustomCodeColumnEntity
import co.brainz.itsm.customCode.entity.CustomCodeColumnPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomCodeColumnRepository: JpaRepository<CustomCodeColumnEntity, CustomCodeColumnPk> {
    fun findByOrderByCustomCodeColumnNameAsc(): List<CustomCodeColumnEntity>
}