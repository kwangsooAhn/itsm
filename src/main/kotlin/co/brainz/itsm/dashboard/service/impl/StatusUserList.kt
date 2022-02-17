/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl

import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.dashboard.dto.TemplateComponentConfig
import co.brainz.itsm.dashboard.dto.TemplateUserRequestListDto
import co.brainz.itsm.dashboard.repository.DashboardTemplateRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.instance.constants.WfInstanceConstants

class StatusUserList(
    private val currentSessionUser: CurrentSessionUser,
    private val wfDocumentRepository: WfDocumentRepository,
    private val dashboardTemplateRepository: DashboardTemplateRepository
) : co.brainz.itsm.dashboard.service.impl.TemplateComponent {

    override fun getResult(component: TemplateComponentConfig): MutableList<TemplateUserRequestListDto> {
        val target = component.target as LinkedHashMap<String, List<String>>
        val documentList = wfDocumentRepository.findDocumentEntityList(target["documents"] as MutableList<String>)
        val userKey = currentSessionUser.getUserKey()
        val userRequestList = mutableListOf<TemplateUserRequestListDto>()

        for (document in documentList) {
            val documentCount = dashboardTemplateRepository.findDocumentRunningByUserKey(
                document.documentId, userKey, WfInstanceConstants.Status.RUNNING.code
            )
            userRequestList.add(
                TemplateUserRequestListDto(
                    documentId = document.documentId,
                    documentName = document.documentName,
                    count = documentCount
                )
            )
        }
        return userRequestList
    }
}
