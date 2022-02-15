/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service

import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.dashboard.constants.DashboardConstants
import co.brainz.itsm.dashboard.dto.TemplateComponentConfig
import co.brainz.itsm.dashboard.dto.TemplateComponentData
import co.brainz.itsm.dashboard.dto.TemplateDto
import co.brainz.itsm.dashboard.dto.TemplateUserRequestListDto
import co.brainz.itsm.dashboard.repository.DashboardTemplateRepository
import co.brainz.itsm.dashboard.service.impl.TemplateComponentFactory
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.instance.constants.WfInstanceConstants
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DashboardTemplateService(
    private val currentSessionUser: CurrentSessionUser,
    private val dashboardTemplateRepository: DashboardTemplateRepository,
    private val templateComponentFactory: TemplateComponentFactory,
    private val wfDocumentRepository: WfDocumentRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 템플릿 정보 조회
     */
    fun getTemplate(): TemplateDto {
        val templateId = currentSessionUser.getUserTemplateId()
        val templateComponentList = this.getTemplateComponentConfigList(templateId)
        val result = this.getTemplateComponentResult(templateComponentList)
        // TODO: TemplateDto 에 화면에 전송할 추가 정보가 존재할 경우 추가
        return TemplateDto(
            templateId = templateId,
            result = result
        )
    }

    /**
     * 템플릿의 컴포넌트별 설정 정보 조회
     */
    private fun getTemplateComponentConfigList(templateId: String): List<TemplateComponentConfig> {
        val template = dashboardTemplateRepository.findById(templateId).get()
        val templateConfig = mapper.readValue(template.templateConfig, LinkedHashMap::class.java)

        return mapper.convertValue(
            templateConfig["components"],
            TypeFactory.defaultInstance().constructCollectionType(List::class.java, TemplateComponentConfig::class.java)
        )
    }

    /**
     * 템플릿 컴포넌트별 조회 (데이터 포함)
     */
    private fun getTemplateComponentResult(templateComponentList: List<TemplateComponentConfig>): List<TemplateComponentData> {
        val templateComponentResultList = mutableListOf<TemplateComponentData>()

        templateComponentList.forEach { component ->
            var result: Any? = null
            when (component.key) {
                DashboardConstants.TemplateComponent.STATUS_USER_LIST.code -> {
                    result = this.getRequestStatusUserList(component.target as LinkedHashMap<String, List<String>>)
                }
            }
            templateComponentResultList.add(
                TemplateComponentData(
                    key = component.key,
                    title = component.title,
                    result = result
                )
            )
        }
        return templateComponentResultList
    }

    /**
     *  개인 요청 현황판 조회
     */
    private fun getRequestStatusUserList(target: LinkedHashMap<String, List<String>>): List<TemplateUserRequestListDto> {
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
