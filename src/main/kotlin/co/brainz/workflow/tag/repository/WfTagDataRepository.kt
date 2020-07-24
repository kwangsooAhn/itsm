package co.brainz.workflow.tag.repository

import co.brainz.workflow.tag.entity.WfTagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfTagDataRepository : JpaRepository<WfTagEntity, String>
