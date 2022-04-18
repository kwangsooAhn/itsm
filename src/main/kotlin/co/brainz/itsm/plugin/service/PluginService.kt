/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.plugin.dto.PluginParamDto
import co.brainz.itsm.plugin.entity.PluginEntity
import co.brainz.itsm.plugin.repository.PluginRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class PluginService(
    private val pluginFactory: PluginFactory,
    private val pluginRepository: PluginRepository
) {

    /**
     * [pluginId] 에 따른 처리
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun executePlugin(
        pluginId: String,
        pluginParam: PluginParamDto,
        body: String?,
        param: LinkedHashMap<String, Any>
    ): ZResponse {
        val pluginServiceImpl = pluginFactory.getFactory(pluginId)
        pluginServiceImpl.initialize(this.getPlugin(pluginId), pluginParam, body)
        return pluginServiceImpl.execute(param)
    }

    /**
     * Plugin 조회
     */
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

    /**
     * Plugin 목록 조회
     */
    fun getPlugins(): ZResponse {
        return ZResponse(
            data = pluginRepository.getPlugins()
        )
    }
}
