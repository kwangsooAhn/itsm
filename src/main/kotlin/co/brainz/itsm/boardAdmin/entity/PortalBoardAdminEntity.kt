package co.brainz.itsm.boardAdmin.entity

import co.brainz.framework.auditor.AliceMetaEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "portal_board_admin")
class PortalBoardAdminEntity(
        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        @Column(name = "board_admin_id", length = 128)
        var boardAdminId: String,

        @Column(name = "board_admin_title", length = 512)
        var boardAdminTitle: String?,

        @Column(name = "board_admin_desc")
        var boardAdminDesc: String?,

        @Column(name = "board_admin_sort")
        var boardAdminSort: Int?,

        @Column(name = "board_use_yn")
        var boardUseYn: Boolean = false,

        @Column(name = "answer_yn")
        var answerYn: Boolean = false,

        @Column(name = "comment_yn")
        var commentYn: Boolean = false,

        @Column(name = "category_yn")
        var categoryYn: Boolean = false,

        @Column(name = "attach_yn")
        var attachYn: Boolean = false,

        @Column(name = "attach_file_size")
        var attachFileSize: Long?
): Serializable, AliceMetaEntity()