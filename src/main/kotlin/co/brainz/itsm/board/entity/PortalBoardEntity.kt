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
        var boardCategoryId: String? = "",

        @Column(name = "board_seq", insertable = true, updatable = false)
        var boardSeq: Long,

        @Column(name = "board_group_no", insertable = true, updatable = false)
        var boardGroupNo:Long,

        @Column(name = "board_level_no")
        var boardLevelNo: Long,

        @Column(name = "board_order_seq")
        var boardOrderSeq:Long,

        @Column(name = "board_title", length = 512)
        var boardTitle: String?,

        @Column(name = "board_conents")
        var boardConents: String?

): Serializable, AliceMetaEntity()