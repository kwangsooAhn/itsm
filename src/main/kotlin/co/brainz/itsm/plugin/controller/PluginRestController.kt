/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.controller

import co.brainz.itsm.plugin.service.PluginService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/plugins")
class PluginRestController(
    private val pluginService: PluginService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{pluginId}")
    fun executePlugin(@PathVariable pluginId: String, @RequestBody body: String?) {
        pluginService.executePlugin(pluginId, body)
    }
}
