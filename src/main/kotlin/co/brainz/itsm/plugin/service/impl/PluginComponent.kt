/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.service.impl

import co.brainz.itsm.plugin.entity.PluginEntity
import org.springframework.stereotype.Component

@Component
abstract class PluginComponent {

    abstract fun execute(plugin: PluginEntity, body: Any?)
}
