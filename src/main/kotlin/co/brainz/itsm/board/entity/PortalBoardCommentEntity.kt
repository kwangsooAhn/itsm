package co.brainz.itsm.board.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "portal_board_comment")
class PortalBoardCommentEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "board_comment_id", length = 128)
    var boardCommentId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    val commentBoard: PortalBoardEntity,

    @Column(name = "board_comment_contents", length = 512)
    var boardCommentContents: String

) : Serializable, AliceMetaEntity()
