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
@Table(name = "portal_board_category")
class PortalBoardCategoryEntity(
        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        @Column(name = "board_category_id", length = 128)
        var boardCategoryId: String,

        @Column(name = "board_admin_id", length = 128)
        var boardAdminId: String,

        @Column(name = "board_category_name", length = 128)
        var boardCategoryName: String?,

        @Column(name = "board_category_sort")
        var boardCategorySort: Int?
): Serializable, AliceMetaEntity()