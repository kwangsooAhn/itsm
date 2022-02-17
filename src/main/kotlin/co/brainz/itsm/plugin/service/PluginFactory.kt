/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.itsm.plugin.constants.PluginConstants
import co.brainz.itsm.plugin.service.impl.FocsComponent
import co.brainz.itsm.plugin.service.impl.PluginComponent
import org.springframework.stereotype.Component


@Component
class PluginFactory {

    fun getFactory(pluginId: String): PluginComponent {
        return when (pluginId) {
            PluginConstants.PluginId.FOCS.code -> FocsComponent()
            else -> throw AliceException(AliceErrorConstants.ERR, "Plugin not found.")
        }
    }
}
