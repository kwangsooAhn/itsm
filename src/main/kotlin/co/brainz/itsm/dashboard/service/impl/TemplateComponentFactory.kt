/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.dashboard.constants.DashboardConstants
import co.brainz.itsm.dashboard.repository.DashboardTemplateRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import org.springframework.stereotype.Component

@Component
class TemplateComponentFactory(
    private val organizationRepository: OrganizationRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val wfDocumentRepository: WfDocumentRepository,
    private val dashboardTemplateRepository: DashboardTemplateRepository
) {

    /**
     * [key] 에 따른 관련 컴포넌트 호출
     */
    fun getComponent(key: String): TemplateComponent {
        return when (key) {
            DashboardConstants.TemplateComponent.ORGANIZATION_CHART.code -> OrganizationChart(
                wfDocumentRepository, organizationRepository, dashboardTemplateRepository
            )
            DashboardConstants.TemplateComponent.STATUS_USER_LIST.code -> StatusUserList(
                currentSessionUser, wfDocumentRepository, dashboardTemplateRepository
            )
            DashboardConstants.TemplateComponent.ORGANIZATION_LIST.code -> OrganizationList()
            else -> throw  AliceException(
                AliceErrorConstants.ERR_00005, "Dashboard Template Component not found."
            )
        }
    }
}
