package co.brainz.itsm.board.entity

import co.brainz.framework.auditor.AliceMetaEntity
import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity
import java.io.Serializable
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "portal_board")
class PortalBoardEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "board_id", length = 128)
    var boardId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_admin_id")
    val boardAdmin: PortalBoardAdminEntity,

    @Column(name = "board_category_id ", length = 128)
    var boardCategoryId: String? = "",

    @Column(name = "board_seq", insertable = true, updatable = false)
    var boardSeq: Long,

    @Column(name = "board_group_id", insertable = true, updatable = false)
    var boardGroupId: Long,

    @Column(name = "board_level_id")
    var boardLevelId: Long,

    @Column(name = "board_order_seq")
    var boardOrderSeq: Long,

    @Column(name = "board_title", length = 512)
    var boardTitle: String?,

    @Column(name = "board_contents")
    var boardContents: String?

) : Serializable, AliceMetaEntity() {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commentBoard", cascade = [CascadeType.REMOVE])
    val commentBoard: MutableList<PortalBoardCommentEntity>? = mutableListOf()
}
