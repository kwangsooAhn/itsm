/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.workflow.instance.dto.WfInstanceListInstanceDto

interface DashboardTemplateRepositoryCustom : AliceRepositoryCustom {
    fun findByStatusGroupByUserKey(status: String): List<WfInstanceListInstanceDto>
}
