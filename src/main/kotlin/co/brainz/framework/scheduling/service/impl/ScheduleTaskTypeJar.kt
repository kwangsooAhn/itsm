/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.scheduling.service.impl

import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.scheduling.entity.AliceScheduleHistoryEntity
import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.framework.scheduling.repository.AliceScheduleHistoryRepository
import co.brainz.framework.util.AliceFileUtil
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component


@Component
class ScheduleTaskTypeJar(
    environment: Environment,
    private val aliceScheduleHistoryRepository: AliceScheduleHistoryRepository
) : AliceFileUtil(environment) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getRunnable(taskInfo: AliceScheduleTaskEntity, isImmediately: Boolean): Runnable? {
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
            if (it == AliceConstants.SCHEDULER_COMMAND_JAR) {
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
            processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT)
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
            val process = processBuilder.start()
            process.waitFor(30, TimeUnit.SECONDS)
            val isSuccess = process.exitValue() == 0
            var errorMsg = ""
            if (!isSuccess) {
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                val builder = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                    builder.append(System.getProperty("line.separator"))
                }
                errorMsg = builder.toString()
            }
            aliceScheduleHistoryRepository.save(
                AliceScheduleHistoryEntity(
                    historySeq = 0L,
                    taskId = taskInfo.taskId,
                    immediatelyExecute = isImmediately,
                    executeTime = LocalDateTime.now(),
                    result = isSuccess,
                    errorMessage = errorMsg
                )
            )
        }
        return runnable
    }
}
