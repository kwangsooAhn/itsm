/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl

import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.itsm.customCode.service.CustomCodeService
import co.brainz.itsm.dashboard.constants.DashboardConstants
import co.brainz.itsm.dashboard.dto.OrganizationItem
import co.brainz.itsm.dashboard.dto.TemplateComponentConfig
import co.brainz.itsm.dashboard.dto.TemplateOrganizationListDto
import co.brainz.itsm.dashboard.repository.DashboardTemplateRepository
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.component.repository.WfComponentPropertyRepository
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

class OrganizationList(
    private val wfDocumentRepository: WfDocumentRepository,
    private val organizationRepository: OrganizationRepository,
    private val dashboardTemplateRepository: DashboardTemplateRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfComponentPropertyRepository: WfComponentPropertyRepository,
    private val customCodeService: CustomCodeService
) : TemplateComponent {

    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    private var organizationId = ""

    override fun init(option: Map<String, Any>) {
        if (option["organizationId"] != null) {
            this.organizationId = option["organizationId"].toString()
        }
    }

    override fun getResult(component: TemplateComponentConfig): Any {
        val target = mapper.convertValue(component.target, Map::class.java)
        val documents: List<String> = mapper.convertValue(target["documents"], object : TypeReference<List<String>>() {})
        val items: List<OrganizationItem> = mapper.convertValue(
            target["items"],
            TypeFactory.defaultInstance().constructCollectionType(List::class.java, OrganizationItem::class.java))

        val documentEntities = wfDocumentRepository.getDocumentListByIds(documents.toSet())
        val documentIds = mutableSetOf<String>()
        documentEntities.forEach { documentIds.add(it.documentId) }

        var organizationName = ""
        if (organizationId.isNotEmpty()) {
            val organization = organizationRepository.findByOrganizationId(organizationId)
            organizationName = organization.organizationName.toString()
        }

        // 문서 조회
        val instanceList = dashboardTemplateRepository.organizationRunningDocument(documentIds, this.organizationId)

        return TemplateOrganizationListDto(
            organizationId = this.organizationId,
            organizationName = organizationName,
            columnTitle = this.getValueToArray(items, DashboardConstants.ComponentItemKey.TITLE.code),
            columnWidth = this.getValueToArray(items, DashboardConstants.ComponentItemKey.WIDTH.code),
            columnType = this.getValueToArray(items, DashboardConstants.ComponentItemKey.DATA_TYPE.code),
            contents = this.getContents(items, instanceList)
        )
    }

    /**
     * 설정 정보에 따른 값 조회
     */
    private fun getContents(
        items: List<OrganizationItem>,
        instanceList: List<WfInstanceEntity>
    ): List<Array<Any>> {
        val instanceIds = mutableSetOf<String>()
        instanceList.forEach { instanceIds.add(it.instanceId) }
        val tokenList = wfTokenRepository.getListRunningTokenList(instanceIds)

        val formIds = mutableSetOf<String>()
        instanceList.forEach { formIds.add(it.document.form.formId) }
        val componentList = wfComponentRepository.findByFormIds(formIds)

        val result = mutableListOf<Array<Any>>()
        instanceList.forEach { instance ->
            val valueList = mutableListOf<Any>()
            var value: Any = ""

            val lastToken = tokenList.first { it.instance == instance }
            val instanceComponentList = mutableListOf<WfComponentEntity>()
            componentList.forEach {
                if (it.form == instance.document.form) {
                    instanceComponentList.add(it)
                }
            }

            items.forEach { item ->
                value = when (item.type) {
                    DashboardConstants.ComponentItemType.FIELD.code -> this.getFieldValue(item, instance)
                    DashboardConstants.ComponentItemType.MAPPING.code -> {
                        this.getMappingValue(item, lastToken, instanceComponentList)
                    }
                    else -> ""
                }
                valueList.add(value)
            }
            result.add(valueList.toTypedArray())
        }
        return result
    }

    /**
     * name 값으로 mappingId를 조회하여 데이터 검색
     */
    private fun getMappingValue(
        item: OrganizationItem,
        token: WfTokenEntity,
        componentList: List<WfComponentEntity>
    ): Any {
        var component: WfComponentEntity? = null
        componentList.forEach {
            if (it.mappingId == item.name) {
                component = it
            }
        }

        var value = ""
        if (component != null) {
            val tokenData = wfTokenDataRepository
                .findWfTokenDataEntitiesByTokenTokenIdAndComponentComponentId(token.tokenId, component!!.componentId)
            value = when (component?.componentType) {
                WfComponentConstants.ComponentType.CUSTOM_CODE.code -> {
                    this.getCustomCodeValue(component!!, tokenData.value)
                }
                else -> tokenData.value
            }
        }

        return value
    }

    /**
     * 커스텀 코드 데이터 조회
     */
    private fun getCustomCodeValue(component: WfComponentEntity, tokenValue: String): String {
        var value = ""
        val componentProperties = wfComponentPropertyRepository.findByComponentId(component.componentId)
        var propertyOptions = ""
        componentProperties.forEach {
            if (it.propertyType == WfComponentConstants.ComponentPropertyType.ELEMENT.code) {
                propertyOptions = it.propertyOptions
            }
        }
        if (propertyOptions.isNotEmpty()) {
            val options: Map<String, Any> = mapper.readValue(propertyOptions, object : TypeReference<Map<String, Any>>() {})
            val defaultValueCustomCode = options["defaultValueCustomCode"].toString()
            val customCodeId = defaultValueCustomCode.split("|")[0]
            val customCodeDataList = customCodeService.getCustomCodeData(customCodeId)
            customCodeDataList.data.forEach {
                if (it.code == tokenValue) {
                    value = it.codeName?: ""
                }
            }
        }
        return value
    }

    /**
     * field 의 name 값으로 매핑된 컬럼 데이터 조회
     */
    private fun getFieldValue(
        item: OrganizationItem,
        instance: WfInstanceEntity
    ): Any {
        return when (item.name) {
            DashboardConstants.Column.DOCUMENT_NAME.code -> instance.document.documentName
            DashboardConstants.Column.INSTANCE_STATUS.code -> instance.instanceStatus
            DashboardConstants.Column.DOCUMENT_NO.code -> instance.documentNo ?: ""
            else -> ""
        }
    }

    /**
     * 컴포넌트 item 값을 [itemKey] 에 따라 배열 처리
     */
    private fun getValueToArray(
        items: List<OrganizationItem>,
        itemKey: String
    ): Array<String> {
        val valueList = mutableListOf<String>()
        when (itemKey) {
            DashboardConstants.ComponentItemKey.TITLE.code -> items.forEach { valueList.add(it.title) }
            DashboardConstants.ComponentItemKey.WIDTH.code -> items.forEach { valueList.add(it.width) }
            DashboardConstants.ComponentItemKey.DATA_TYPE.code -> items.forEach { valueList.add(it.dataType) }
        }
        return valueList.toTypedArray()
    }
}
