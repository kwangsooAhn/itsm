/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.resourceManager.repository

import co.brainz.framework.resourceManager.entity.AliceFileOwnMapEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface AliceFileOwnMapRepositoryCustom : AliceRepositoryCustom {

    fun findFileOwnIdAndFileLocUploaded(ownId: String, uploaded: Boolean): List<AliceFileOwnMapEntity>
}
