/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service

import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.dashboard.dto.TemplateComponentConfig
import co.brainz.itsm.dashboard.dto.TemplateComponentData
import co.brainz.itsm.dashboard.dto.TemplateDto
import co.brainz.itsm.dashboard.repository.DashboardTemplateRepository
import co.brainz.itsm.dashboard.service.impl.TemplateComponentFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import co.brainz.itsm.dashboard.constants.DashboardConstants
import co.brainz.itsm.dashboard.dto.TemplateUserRequestListDto
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.instance.constants.WfInstanceConstants

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
        // TODO: TemplateDto 에 화면에 전송할 추가 정보가 존재할 경우 추가
        return TemplateDto(
            templateId = templateId,
            result = this.getTemplateComponentResult(templateComponentList)
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
    private fun getTemplateComponentResult(templateComponentList: List<TemplateComponentConfig>): MutableList<Any> {
        val templateComponentResultList: MutableList<Any> = mutableListOf<Any>()
        templateComponentList.forEach { component ->
            when(component.key) {
                DashboardConstants.TemplateComponent.STATUS_USER_LIST.code -> {
                    templateComponentResultList.add(this.getRequestStatusUserList(component))
                }
            }
        }
        return templateComponentResultList
    }

    private fun getRequestStatusUserList(component: TemplateComponentConfig): TemplateComponentData {
        val target: LinkedHashMap<String, List<String>> = component.target as LinkedHashMap<String, List<String>>
        val documentList: MutableList<String> = target["documents"] as MutableList<String>
        val userKey = currentSessionUser.getUserKey()
        var templateDto: MutableList<TemplateUserRequestListDto> = mutableListOf()


        for (document in documentList) {
            val documentDtoList = dashboardTemplateRepository.findByDocumentIdAndUserKeyAndStatus(document, userKey, WfInstanceConstants.Status.RUNNING.code)
            val documentDto = wfDocumentRepository.findByDocumentId(document)

            templateDto += TemplateUserRequestListDto(
                    documentId = documentDto!!.documentId,
                    documentName = documentDto.documentName,
                    count = documentDtoList
                )
            }


        return TemplateComponentData(
            key = component.key,
            title = component.title,
            result = templateDto
        )
    }
}
