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
@Table(name = "portal_board")
class PortalBoardEntity(
        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        @Column(name = "board_id", length = 128)
        var boardId: String,

        @Column(name = "board_admin_id", length = 128)
        var boardAdminId: String,

        @Column(name = "board_category_id ", length = 128)
        var boardCategoryId : String? = "",

        @Column(name = "board_no", insertable = true, updatable = false)
        var boardNo: Long? = 0,

        @Column(name = "board_title", length = 512)
        var boardTitle: String?,

        @Column(name = "board_conents")
        var boardConents: String?,

        @Column(name = "parent_board_id", length = 128)
        var parentBoardId: String?

): Serializable, AliceMetaEntity()