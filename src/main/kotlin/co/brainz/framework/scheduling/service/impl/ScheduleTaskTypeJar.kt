/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.scheduling.service.impl

import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.framework.util.AliceFileUtil
import java.io.File
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class ScheduleTaskTypeJar(
    environment: Environment
) : AliceFileUtil(environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getRunnable(taskInfo: AliceScheduleTaskEntity): Runnable? {
        var runnable: Runnable? = null

        var jarPath = taskInfo.src ?: ""
        if (jarPath.startsWith("/", true)) {
            jarPath = jarPath.substring(1)
        }
        val jarDir = AliceConstants.SCHEDULE_PLUGINS_HOME + File.separator + jarPath

        // command list
        val commands = taskInfo.executeCommand?.split(" ")
        val command = mutableListOf<String>()

        var logbackConfigurationFile = ""
        var logHome = ""
        commands?.forEach {
            if (it.contains(AliceConstants.PLUGINS_VM_OPTIONS_LOG_CONFIG_FILE)) {
                logbackConfigurationFile = it.split("=")[1]
            }
            if (it.contains(AliceConstants.PLUGINS_VM_OPTIONS_LOG_HOME)) {
                logHome = it.split("=")[1]
            }
        }

        val moduleHome = File(jarDir).absolutePath
        commands?.forEach {
            if (it == "-jar") {
                if (logbackConfigurationFile.isEmpty()) {
                    command.add(AliceConstants.PLUGINS_VM_OPTIONS_LOG_CONFIG_FILE + "=" + moduleHome + "/logback.xml")
                }
                if (logHome.isEmpty()) {
                    command.add(AliceConstants.PLUGINS_VM_OPTIONS_LOG_HOME + "=" + moduleHome + "/logs")
                }
            }
            command.add(it.trim())
        }
        runnable = Runnable {
            val processBuilder = ProcessBuilder(command)
            processBuilder.directory(File(jarDir))
            processBuilder.start()
        }
        return runnable
    }
}
