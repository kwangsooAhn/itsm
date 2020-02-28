package co.brainz.workflow.process.repository

import co.brainz.workflow.process.entity.ElementMstEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ElementMstRepository : JpaRepository<ElementMstEntity, String> {
    fun findByProcId(processId: String): MutableList<ElementMstEntity>
    fun deleteByProcId(processId: String): MutableList<ElementMstEntity>
    fun deleteAllByElemIdIn(ids: MutableList<String>)
}
