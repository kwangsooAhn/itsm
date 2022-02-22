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
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

class StatusUserList(
    private val currentSessionUser: CurrentSessionUser,
    private val wfDocumentRepository: WfDocumentRepository,
    private val dashboardTemplateRepository: DashboardTemplateRepository
) : TemplateComponent {

    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    override fun init(option: Map<String, Any>) {}

    override fun getResult(component: TemplateComponentConfig): MutableList<TemplateUserRequestListDto> {
        val target = mapper.convertValue(component.target, Map::class.java)
        val documents: List<String> = mapper.convertValue(target["documents"], object : TypeReference<List<String>>() {})
        val documentEntities = wfDocumentRepository.getDocumentListByIds(documents.toSet())
        val userRequestCounts = mutableListOf<TemplateUserRequestListDto>()
        documentEntities.forEach { documentEntity ->
            userRequestCounts.add(
                TemplateUserRequestListDto(
                    documentId = documentEntity.documentId,
                    documentName = documentEntity.documentName,
                    count = dashboardTemplateRepository.countByUserRunningDocument(documentEntity, currentSessionUser.getUserKey())
                )
            )
        }
        return userRequestCounts
    }
}
