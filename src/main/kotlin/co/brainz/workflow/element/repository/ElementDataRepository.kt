package co.brainz.workflow.element.repository

import co.brainz.workflow.element.entity.ElementDataEntity
import co.brainz.workflow.element.entity.ElementDataPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ElementDataRepository : JpaRepository<ElementDataEntity, ElementDataPk>
