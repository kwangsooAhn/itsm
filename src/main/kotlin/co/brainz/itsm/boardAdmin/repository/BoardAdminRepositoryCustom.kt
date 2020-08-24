/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.boardAdmin.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.boardAdmin.dto.BoardAdminListDto

interface BoardAdminRepositoryCustom : AliceRepositoryCustom {

    fun findByBoardAdminList(
        search: String,
        offset: Long
    ): List<BoardAdminListDto>
}
