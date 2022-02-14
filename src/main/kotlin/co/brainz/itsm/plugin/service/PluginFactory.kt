/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.itsm.plugin.constants.PluginConstants
import co.brainz.itsm.plugin.service.impl.FirewallComponent
import co.brainz.itsm.plugin.service.impl.PluginComponent
import org.springframework.stereotype.Component


@Component
class PluginFactory {

    fun getFactory(pluginType: String): PluginComponent {
        return when (pluginType) {
            PluginConstants.PluginType.FIREWALL.code -> FirewallComponent()
            else -> throw AliceException(AliceErrorConstants.ERR, "Plugin not found.")
        }
    }
}
