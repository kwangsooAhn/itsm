/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.controller

import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.plugin.constants.PluginConstants
import co.brainz.itsm.plugin.service.PluginService
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/plugins")
class PluginRestController(
    private val pluginService: PluginService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/{pluginId}")
    fun executePlugin(
        @PathVariable pluginId: String,
        @RequestParam tokenId: String,
        @RequestBody body: String?
    ): ZResponse {
        val param: LinkedHashMap<String, Any> = linkedMapOf()
        param[PluginConstants.ASYNCHRONOUS] = true
        return pluginService.executePlugin(pluginId, WfTokenDto(tokenId = tokenId), body, param)
    }

    @GetMapping("")
    fun getPlugins(): ZResponse {
        return pluginService.getPlugins()
    }
}
