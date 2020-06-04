package co.brainz.workflow.engine.tag.repository

import co.brainz.workflow.engine.tag.entity.WfTagEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WfTagRepository : JpaRepository<WfTagEntity, String>, WfTagRepositoryCustom {

}
