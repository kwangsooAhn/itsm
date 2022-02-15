/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.instance.dto.InstanceTemplateDto

interface DashboardTemplateRepositoryCustom : AliceRepositoryCustom {
    fun findByDocumentIdAndUserKeyAndStatus(document: String, userKey: String, status: String): Long
}
