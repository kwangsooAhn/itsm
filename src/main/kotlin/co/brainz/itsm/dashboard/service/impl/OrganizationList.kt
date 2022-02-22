/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl

import co.brainz.itsm.dashboard.dto.TemplateComponentConfig
import co.brainz.itsm.dashboard.repository.DashboardTemplateRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.util.Arrays

class OrganizationList(
    private val wfDocumentRepository: WfDocumentRepository,
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
        val item: List<Map<String, Any>> = mapper.convertValue(target["item"], object : TypeReference<List<Map<String, Any>>>() {})

        val documentEntities = wfDocumentRepository.getDocumentListByIds(documents.toSet())
        val documentIds = mutableSetOf<String>()
        documentEntities.forEach { documentIds.add(it.documentId) }

        //val organizationDocumentList = dashboardTemplateRepository.organizationRunningDocument(documentIds, this.organizationId)


        return "bbb"
        //TODO("Not yet implemented")
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
