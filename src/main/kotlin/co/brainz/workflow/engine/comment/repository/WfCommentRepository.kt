package co.brainz.workflow.engine.comment.repository

import co.brainz.workflow.engine.comment.entity.WfCommentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WfCommentRepository : JpaRepository<WfCommentEntity, String>, WfCommentRepositoryCustom {

}
