package co.brainz.workflow.tag.repository

import co.brainz.workflow.tag.entity.WfTagMapEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WfTagRepository : JpaRepository<WfTagMapEntity, String>, WfTagRepositoryCustom
