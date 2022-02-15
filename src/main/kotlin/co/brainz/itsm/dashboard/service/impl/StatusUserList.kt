/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl

import org.springframework.stereotype.Component
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.dashboard.dto.TemplateComponentConfig
import co.brainz.itsm.dashboard.dto.TemplateUserRequestListDto
import co.brainz.itsm.dashboard.repository.DashboardTemplateRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.instance.constants.WfInstanceConstants

@Component
class StatusUserList(
    private val currentSessionUser: CurrentSessionUser,
    private val wfDocumentRepository: WfDocumentRepository,
    private val dashboardTemplateRepository: DashboardTemplateRepository
) : co.brainz.itsm.dashboard.service.impl.TemplateComponent {

    override fun getResult(component: TemplateComponentConfig): MutableList<TemplateUserRequestListDto> {
        val target: LinkedHashMap<String,List<String>> = component.target as LinkedHashMap<String,List<String>>
        val documentList: MutableList<String> = target["documents"] as MutableList<String>
        val userKey = currentSessionUser.getUserKey()
        val userRequestList: MutableList<TemplateUserRequestListDto> = mutableListOf()

        for (document in documentList) {
            val documentDto = wfDocumentRepository.findByDocumentId(document)
            val documentDtoList = dashboardTemplateRepository.findByDocumentIdAndUserKeyAndStatus(
                document, userKey, WfInstanceConstants.Status.RUNNING.code
            )
            userRequestList.add(
                TemplateUserRequestListDto(
                    documentId = documentDto!!.documentId,
                    documentName = documentDto.documentName,
                    count = documentDtoList
                )
            )
        }
        return userRequestList
    }

}
