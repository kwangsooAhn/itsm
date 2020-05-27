package co.brainz.workflow.comment.repository

import co.brainz.workflow.comment.entity.WfCommentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WfCommentRepository : JpaRepository<WfCommentEntity, String>, WfCommentRepositoryCustom {

}
