package co.brainz.workflow.tag.repository

import co.brainz.workflow.tag.entity.WfTagEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WfTagRepository : JpaRepository<WfTagEntity, String>,
    WfTagRepositoryCustom {

}
