package co.brainz.workflow.process.repository

import co.brainz.workflow.process.entity.ElementDataEntity
import co.brainz.workflow.process.entity.ElementDataPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ElementDataRepository : JpaRepository<ElementDataEntity, ElementDataPk> {
    fun findByElemId(elementId: String): ElementDataEntity
    fun deleteByElemId(elementId: String): ElementDataEntity
}
