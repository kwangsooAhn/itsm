package co.brainz.itsm.instance.repository

import co.brainz.itsm.instance.entity.WfCommentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<WfCommentEntity, String>, CommentRepositoryCustom
