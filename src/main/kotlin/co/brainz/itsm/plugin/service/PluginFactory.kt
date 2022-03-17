/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.itsm.plugin.constants.PluginConstants
import co.brainz.itsm.plugin.repository.PluginHistoryRepository
import co.brainz.itsm.plugin.service.impl.FocsComponent
import co.brainz.itsm.plugin.service.impl.PluginComponent
import org.springframework.stereotype.Component

@Component
class PluginFactory(
    private val pluginHistoryRepository: PluginHistoryRepository
) {

    /**
     * [pluginId] 에 따른 구현 class 호출
     */
    fun getFactory(pluginId: String): PluginComponent {
        return when (pluginId) {
            PluginConstants.PluginId.FOCS.code -> FocsComponent(pluginHistoryRepository)
            else -> throw AliceException(AliceErrorConstants.ERR, "Plugin not found.")
        }
    }
}