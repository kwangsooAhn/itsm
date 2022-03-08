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
import com.fasterxml.jackson.core.type.TypeReference
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
    private val templateComponentFactory: TemplateComponentFactory
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
     * 단일 컴포넌트 조회 (데이터 포함)
     */
    fun getTemplateComponent(templateId: String, componentKey: String, options: Any?): TemplateComponentData {
        var option: Map<String, Any> = mutableMapOf()
        if (options != null) {
            option = mapper.convertValue(options, object : TypeReference<Map<String, Any>>() {})
        }
        val templateComponentConfigList = this.getTemplateComponentConfigList(templateId)
        val component = templateComponentConfigList.first { it.key == componentKey }
        return this.getTemplateComponentData(component, option)
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
            templateComponentResultList.add(this.getTemplateComponentData(component, mutableMapOf()))
        }
        return templateComponentResultList
    }

    /**
     * 컴포넌트 데이터 조회
     */
    private fun getTemplateComponentData(component: TemplateComponentConfig, option: Map<String, Any>): TemplateComponentData {
        val templateComponent = templateComponentFactory.getComponent(component.key)
        if (!option.isNullOrEmpty()) {
            templateComponent.init(option)
        }
        return TemplateComponentData(
            key = component.key,
            title = component.title,
            result = templateComponent.getResult(component)
        )
    }
}
