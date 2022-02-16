/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.itsm.dashboard.constants.DashboardConstants
import org.springframework.stereotype.Component

@Component
class TemplateComponentFactory {

    /**
     * [key] 에 따른 관련 컴포넌트 호출
     */
    fun getComponent(key: String): TemplateComponent {
        return when (key) {
            DashboardConstants.TemplateComponent.ORGANIZATION_CHART.code -> OrganizationChart()
            DashboardConstants.TemplateComponent.STATUS_USER_LIST.code -> StatusUserList()
            DashboardConstants.TemplateComponent.ORGANIZATION_LIST.code -> OrganizationList()
            else -> throw  AliceException(
                AliceErrorConstants.ERR_00005, "Dashboard Template Component not found."
            )
        }
    }
}
