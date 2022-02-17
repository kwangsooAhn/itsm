package co.brainz.itsm.plugin.service.impl

import co.brainz.framework.constants.AliceConstants
import co.brainz.itsm.plugin.entity.PluginEntity
import java.io.File
import org.springframework.stereotype.Component

@Component
class FocsComponent: PluginComponent() {

    override fun execute(plugin: PluginEntity, body: Any?) {
        val home = File(plugin.pluginLocation).absolutePath
        val commands = plugin.pluginCommand.split(" ")
        val command = mutableListOf<String>()
        commands.forEach {
            command.add(AliceConstants.PLUGINS_VM_OPTIONS_LOG_CONFIG_FILE + "=" + home + "/logback.xml")
            command.add(AliceConstants.PLUGINS_VM_OPTIONS_LOG_HOME + "=" + home + "/logs")
            command.add(it.trim())
        }
        println(command)

        // TODO: body Parsing
        var runnable: Runnable?
        runnable = Runnable {
            val processBuilder = ProcessBuilder(command)
            processBuilder.directory(File(plugin.pluginLocation))
            processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT)
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
            val process = processBuilder.start()
            val isSuccess = process.exitValue() == 0
            var errorMsg = ""
            println(isSuccess)
        }

    }

    /*fun getDuplicate() {

    }

    fun getRuleCheck() {

    }

    fun setApplicationForm() {
        // TODO: plugin 실행
    }*/


}
