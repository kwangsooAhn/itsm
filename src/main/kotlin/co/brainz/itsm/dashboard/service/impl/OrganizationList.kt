/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl

import co.brainz.framework.organization.repository.OrganizationRepository
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
import java.util.Arrays

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

        // contents
        val contents = this.getContents(items, organizationDocumentList)




        //column info
        val organizationListDto = TemplateOrganizationListDto(
            organizationId = this.organizationId,
            organizationName = organizationName,
            columnTitle = this.getColumnTitle(items),
            columnWidth = this.getColumnWidth(items),
            columnType = this.getColumnType(items),
            contents = contents
        )

        return organizationListDto
    }


    private fun getContents(items: List<OrganizationItem>, organizationDocumentList: List<WfInstanceEntity>): List<Array<String>> {
        val result = mutableListOf<Array<String>>()

        // Dummy data
        val dummyData = this.getDummyData()
        organizationDocumentList.forEachIndexed { index, wfInstanceEntity ->
            result.add(dummyData[(0..2).random()])
        }
        return result
    }

    private fun getDummyData(): List<Array<String>> {
        val result = mutableListOf<Array<String>>()
        result.add(
            arrayOf("", "본부2", "장애 신고", "장애 신고 합니다.", "2022-01-13T01:44:00.000Z", "2022-01-20T109:00:00.000Z", "진행중", "ADMIN", "ADMIN", "긴급", "CSR-20220113-001")
        )
        result.add(
            arrayOf("", "본부2", "단순 문의", "공지사항 추가 문의", "2022-01-15T02:31:00.000Z", "2022-01-20T09:00:00.000Z", "진행중", "ADMIN", "ADMIN", "긴급", "CSR-20220115-001")
        )
        result.add(
            arrayOf("", "본부2", "단순 문의", "개인 정보 보안 문의드립니다.", "2022-01-21T03:12:00.000Z", "2022-01-24T09:00:00.000Z", "진행중", "ADMIN", "ADMIN", "긴급", "CSR-20220221-001")
        )
        return result
    }

    private fun getColumnTitle(items: List<OrganizationItem>): Array<String> {
        val titleList = mutableListOf<String>()
        items.forEach { titleList.add(it.title) }
        return titleList.toTypedArray()
    }

    private fun getColumnWidth(items: List<OrganizationItem>): Array<String> {
        val widthList = mutableListOf<String>()
        items.forEach { widthList.add(it.width) }
        return widthList.toTypedArray()
    }

    private fun getColumnType(items: List<OrganizationItem>): Array<String> {
        val typeList = mutableListOf<String>()
        items.forEach { typeList.add(it.dataType) }
        return typeList.toTypedArray()
    }


    fun toCamelCase(str: String): String {
        val words = str.split("[-_]".toRegex()).toTypedArray()
        return Arrays.stream(words, 1, words.size).map { s: String ->
            s.substring(0, 1).toUpperCase() + s.substring(1)
        }.reduce(
            words[0]
        ) { obj: String, strValue: String -> obj + strValue }
    }

}
