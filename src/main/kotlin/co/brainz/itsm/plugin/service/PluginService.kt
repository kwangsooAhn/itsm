/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.service

import org.springframework.stereotype.Service

@Service
class PluginService(
    private val pluginFactory: PluginFactory
) {

    fun executePlugin(pluginType: String, body: Any?) {
        pluginFactory.getFactory(pluginType).execute(body)
    }
}
