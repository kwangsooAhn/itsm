package co.brainz.itsm.plugin.service.impl

import co.brainz.itsm.plugin.entity.PluginEntity
import org.springframework.stereotype.Component

@Component
class FocsComponent: PluginComponent() {

    override fun execute(plugin: PluginEntity, body: Any?) {
        println("Firewall!!!")
        // TODO: body Parsing
    }

    /*fun getDuplicate() {

    }

    fun getRuleCheck() {

    }

    fun setApplicationForm() {
        // TODO: plugin 실행
    }*/


}
