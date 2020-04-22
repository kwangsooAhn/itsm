package co.brainz.itsm.board.entity

import co.brainz.framework.auditor.AliceMetaEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "portal_board_comment")
class PortalBoardCommentEntity(
        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        @Column(name = "board_comment_id", length = 128)
        var boardCommentId: String,

        @Column(name = "board_id", length = 128)
        var boardId: String,

        @Column(name = "board_comment_conents", length = 512)
        var boardCommentConents: String

): Serializable, AliceMetaEntity()