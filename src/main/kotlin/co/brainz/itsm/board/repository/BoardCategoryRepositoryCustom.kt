/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.board.dto.BoardCategoryDto

interface BoardCategoryRepositoryCustom : AliceRepositoryCustom {

    fun findByCategoryList(
        boardAdminId: String
    ): List<BoardCategoryDto>
}
