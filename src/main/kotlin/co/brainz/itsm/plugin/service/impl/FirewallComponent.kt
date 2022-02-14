package co.brainz.itsm.plugin.service.impl

import org.springframework.stereotype.Component

@Component
class FirewallComponent: PluginComponent {

    override fun execute(body: Any?) {
        println("Firewall!!!")
        // TODO: body Parsing
    }

    fun getDuplicate() {

    }

    fun getRuleCheck() {

    }

    fun setApplicationForm() {
        // TODO: plugin 실행
    }


}
