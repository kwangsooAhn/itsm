/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.itsm.plugin.entity.PluginEntity
import co.brainz.itsm.plugin.repository.PluginRepository
import org.springframework.stereotype.Service

@Service
class PluginService(
    private val pluginFactory: PluginFactory,
    private val pluginRepository: PluginRepository
) {

    fun executePlugin(pluginId: String, body: Any?) {
        pluginFactory.getFactory(pluginId).execute(this.getPlugin(pluginId), body)
    }

    private fun getPlugin(pluginId: String): PluginEntity {
        val plugin = pluginRepository.findById(pluginId)
        if (!plugin.isPresent) {
            throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[Plugin Entity]"
            )
        }
        return plugin.get()
    }
}
