package co.brainz.itsm.board.dto

import co.brainz.itsm.board.entity.PortalBoardAdminEntity

data class BoardArticleSaveDto(
    var boardId: String = "",
    var boardAdminId: String = "",
    var boardAdmin: PortalBoardAdminEntity?,
    var boardCategoryId: String? = null,
    var boardSeq: Long? = 0,
    var boardGroupId: Long? = 0,
    var boardLevelId: Long? = 0,
    var boardOrderSeq: Long? = 0,
    var boardTitle: String? = "",
    var boardContents: String? = "",
    var fileSeqList: List<Long>? = null,
    var delFileSeqList: List<Long>? = null
)
