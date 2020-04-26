package co.brainz.itsm.board.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "portal_board_read")
class PortalBoardReadEntity(
    @Id
    @Column(name = "board_id", length = 128)
    var boardId: String? = "",

    @Column(name = "board_read_count", length = 128)
    var boardReadCount: Long? = 0
) : Serializable
