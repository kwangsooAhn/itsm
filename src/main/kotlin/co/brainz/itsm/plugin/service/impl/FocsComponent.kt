/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.service.impl

import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.itsm.plugin.constants.PluginConstants
import co.brainz.itsm.plugin.dto.PluginDto
import co.brainz.itsm.plugin.service.PluginHistoryService
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.token.repository.WfTokenDataRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FocsComponent(
    pluginHistoryService: PluginHistoryService,
    aliceTagRepository: AliceTagRepository,
    wfTokenDataRepository: WfTokenDataRepository,
    wfComponentRepository: WfComponentRepository
) : PluginComponent(
    pluginHistoryService,
    aliceTagRepository,
    wfTokenDataRepository,
    wfComponentRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    enum class FocsType(val code: String) {
        ASSESSMENT("assessment"),
        DUPLICATE("duplicate"),
        APPLICATION_FORM("application-form")
    }

    /**
     * initialize
     */
    override fun constructor() {
        val pluginData = PluginDto(
            pluginId = this.plugin.pluginId
        )
        // body 가 없을 경우 WF, 있을 경우 화면에서 호출
        if (super.body.isNullOrEmpty()) {
            pluginData.data = super.getPluginDataByTag()
        } else {
            pluginData.data = super.body
        }
        super.pluginHistory.pluginData = mapper.writeValueAsString(pluginData)
        super.pluginHistory.pluginParam = mapper.writeValueAsString(super.pluginParam)
    }

    /**
     * FOCS 플러그인 실행
     */
    override fun execute(param: LinkedHashMap<String, Any>): ZResponse {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val target = mapper.convertValue(param[PluginConstants.ASYNCHRONOUS], Boolean::class.java)
        var response: ZResponse
        when (target) {
            true -> {
                response = super.executeJar(listOf(FocsType.DUPLICATE.code)) // 중복 검사
                val returnMap: LinkedHashMap<String, Boolean> =
                    mapper.convertValue(response.data, object : TypeReference<Map<String, Boolean>>() {})
                val isSuccess = returnMap["result"].toString().toBoolean()
                if (isSuccess) {
                    response = super.executeJar(listOf(FocsType.ASSESSMENT.code)) // 규정 감사
                }
            }
            false -> {
                response = super.executeJar(listOf(FocsType.APPLICATION_FORM.code)) // 신청서 등록
            }
        }
        return response
    }
}
