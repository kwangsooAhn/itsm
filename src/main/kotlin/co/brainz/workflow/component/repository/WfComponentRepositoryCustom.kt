/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.component.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.workflow.component.entity.WfComponentEntity

interface WfComponentRepositoryCustom : AliceRepositoryCustom {
    fun findByFormIds(formIds: Set<String>): List<WfComponentEntity>
}
