/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl

import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.itsm.dashboard.constants.DashboardConstants
import co.brainz.itsm.dashboard.dto.OrganizationItem
import co.brainz.itsm.dashboard.dto.TemplateComponentConfig
import co.brainz.itsm.dashboard.dto.TemplateOrganizationListDto
import co.brainz.itsm.dashboard.repository.DashboardTemplateRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.instance.entity.WfInstanceEntity
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

class OrganizationList(
    private val wfDocumentRepository: WfDocumentRepository,
    private val organizationRepository: OrganizationRepository,
    private val dashboardTemplateRepository: DashboardTemplateRepository
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
        val organizationDocumentList = dashboardTemplateRepository.organizationRunningDocument(documentIds, this.organizationId)

        return TemplateOrganizationListDto(
            organizationId = this.organizationId,
            organizationName = organizationName,
            columnTitle = this.getValueToArray(items, DashboardConstants.ComponentItemKey.TITLE.code),
            columnWidth = this.getValueToArray(items, DashboardConstants.ComponentItemKey.WIDTH.code),
            columnType = this.getValueToArray(items, DashboardConstants.ComponentItemKey.DATA_TYPE.code),
            contents = this.getContents(items, organizationDocumentList)
        )
    }

    /**
     * 설정 정보에 따른 값 조회
     */
    private fun getContents(
        items: List<OrganizationItem>,
        organizationDocumentList: List<WfInstanceEntity>
    ): List<Array<Any>> {
        val result = mutableListOf<Array<Any>>()
        organizationDocumentList.forEach { organizationDocument ->
            val valueList = mutableListOf<Any>()
            var value: Any = ""
            items.forEach { item ->
                value = when (item.type) {
                    DashboardConstants.ComponentItemType.FIELD.code -> this.getFieldValue(item, organizationDocument)
                    DashboardConstants.ComponentItemType.TAG.code -> this.getTagValue(item, organizationDocument)
                    else -> ""
                }
                valueList.add(value)
            }
            result.add(valueList.toTypedArray())
        }
        return result
    }

    // MappingId 변경
    private fun getTagValue(
        item: OrganizationItem,
        organizationDocument: WfInstanceEntity
    ): Any {
        return ""
    }

    /**
     * field 의 name 값으로 매핑된 컬럼 데이터 조회
     */
    private fun getFieldValue(
        item: OrganizationItem,
        organizationDocument: WfInstanceEntity
    ): Any {
        return when (item.name) {
            DashboardConstants.Column.DOCUMENT_NAME.code -> organizationDocument.document.documentName
            DashboardConstants.Column.INSTANCE_STATUS.code -> organizationDocument.instanceStatus
            DashboardConstants.Column.DOCUMENT_NO.code -> organizationDocument.documentNo ?: ""
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
