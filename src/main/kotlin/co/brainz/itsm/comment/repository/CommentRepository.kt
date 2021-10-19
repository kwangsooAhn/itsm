package co.brainz.itsm.comment.repository

import co.brainz.itsm.comment.entity.WfCommentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<WfCommentEntity, String>, CommentRepositoryCustom
