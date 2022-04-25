/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.itsm.plugin.constants.PluginConstants
import co.brainz.itsm.plugin.service.impl.FocsComponent
import co.brainz.itsm.plugin.service.impl.PluginComponent
import co.brainz.workflow.token.repository.WfTokenDataRepository
import org.springframework.stereotype.Component

@Component
class PluginFactory(
    private val pluginHistoryService: PluginHistoryService,
    private val aliceTagRepository: AliceTagRepository,
    private val wfTokenDataRepository: WfTokenDataRepository
) {

    /**
     * [pluginId] 에 따른 구현 class 호출
     */
    fun getFactory(pluginId: String): PluginComponent {
        return when (pluginId) {
            PluginConstants.PluginId.FOCS.code -> FocsComponent(pluginHistoryService, aliceTagRepository, wfTokenDataRepository)
            else -> throw AliceException(AliceErrorConstants.ERR, "Plugin not found.")
        }
    }
}
