/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.service.impl

import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceUtil
import co.brainz.itsm.plugin.dto.PluginDto
import co.brainz.itsm.plugin.entity.PluginEntity
import co.brainz.itsm.plugin.entity.PluginHistoryEntity
import co.brainz.itsm.plugin.repository.PluginHistoryRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FocsComponent(
    private val pluginHistoryRepository: PluginHistoryRepository
): PluginComponent() {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    private var pluginsDir = ""

    enum class FocsType(val code: String) {
        ASSESSMENT("assessment"),
        DUPLICATE("duplicate"),
        APPLICATION_FORM("application-form")
    }

    /**
     * 환경설정
     */
    override fun init(pluginsDir: String?) {
        this.pluginsDir = pluginsDir ?: ""
    }

    /**
     * 실행 (화면에서 버튼 클릭시 규정 감사 + 중복 검사)
     */
    override fun execute(plugin: PluginEntity, body: String?): ZResponse {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val data = mapper.readValue(body, PluginDto::class.java)
        data.pluginId = plugin.pluginId
        // 중복체크
        var response = this.runDuplicate(plugin, data)
        val returnMap: LinkedHashMap<String, Boolean> = mapper.convertValue(response.data, object : TypeReference<Map<String, Boolean>>() {})
        val isSuccess = returnMap["result"].toString().toBoolean()
        if (isSuccess) {
            response = this.runAssessment(plugin, data)
        }
        return response
    }

    /**
     * 중복 검사
     */
    fun runDuplicate(plugin: PluginEntity, data: PluginDto): ZResponse {
        val command = this.getCommand(FocsType.DUPLICATE.code, plugin, data)
        return this.executeJar(plugin, command)
    }

    /**
     * 규정 감사
     */
    fun runAssessment(plugin: PluginEntity, data: PluginDto): ZResponse {
        val command = this.getCommand(FocsType.ASSESSMENT.code, plugin, data)
        return this.executeJar(plugin, command)
    }

    /**
     * 신청
     */
    fun runApplicationForm(plugin: PluginEntity, data: PluginDto): ZResponse {
        val command = this.getCommand(FocsType.APPLICATION_FORM.code, plugin, data)
        return this.executeJar(plugin, command)
    }

    /**
     * 실행 명령어 생성
     */
    private fun getCommand(focsType: String, plugin: PluginEntity, data: PluginDto): MutableList<String> {
        val home = File(pluginsDir + plugin.pluginLocation).absolutePath
        val commands = plugin.pluginCommand.split(" ")
        val command = mutableListOf<String>()
        commands.forEach {
            if (it == AliceConstants.SCHEDULER_COMMAND_JAR) {
                command.add(AliceConstants.PLUGINS_VM_OPTIONS_LOG_CONFIG_FILE + "=" + home + "/logback.xml")
                command.add(AliceConstants.PLUGINS_VM_OPTIONS_LOG_HOME + "=" + home + "/logs")
            }
            command.add(it.trim())
        }
        command.add(focsType)
        data.data?.let { command.add(mapper.writeValueAsString(data.data)) }
        return command
    }

    /**
     * jar 파일 실행 + 이력
     */
    private fun executeJar(plugin: PluginEntity, command: MutableList<String>): ZResponse {
        var errorMsg = ""
        var isSuccess = true
        val startDateTime = LocalDateTime.now()
        try {
            val runnable = Runnable {
                val process = this.getProcessBuilder(plugin, command).start()
                process.waitFor(60, TimeUnit.SECONDS)
                isSuccess = process.exitValue() == 0
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
            }
            runnable.run()
        } catch (e: Exception) {
            isSuccess = false
            errorMsg = AliceUtil().printStackTraceToString(e)
        }

        // history
        pluginHistoryRepository.save(
            PluginHistoryEntity(
                historyId = "",
                pluginId = plugin.pluginId,
                message = errorMsg,
                startDt = startDateTime,
                endDt = LocalDateTime.now(),
                pluginParam = command.joinToString(" "),
                pluginResult = isSuccess.toString()
            )
        )

        val resultMap = LinkedHashMap<String, Boolean>()
        resultMap["result"] = isSuccess
        return ZResponse(
            message = errorMsg,
            data = resultMap
        )
    }

    /**
     * ProcessBuilder 생성
     */
    private fun getProcessBuilder(plugin: PluginEntity, command: MutableList<String>): ProcessBuilder {
        val processBuilder = ProcessBuilder(command)
        processBuilder.directory(File(pluginsDir + plugin.pluginLocation))
        processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT)
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
        return processBuilder
    }
}
