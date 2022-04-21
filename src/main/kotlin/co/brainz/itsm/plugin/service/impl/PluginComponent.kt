/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.service.impl

import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.framework.util.AliceUtil
import co.brainz.itsm.plugin.dto.PluginParamDto
import co.brainz.itsm.plugin.entity.PluginEntity
import co.brainz.itsm.plugin.entity.PluginHistoryEntity
import co.brainz.itsm.plugin.repository.PluginHistoryRepository
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.token.repository.WfTokenDataRepository
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
abstract class PluginComponent(
    val pluginHistoryRepository: PluginHistoryRepository,
    val aliceTagRepository: AliceTagRepository,
    val wfTokenDataRepository: WfTokenDataRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    protected val pluginsDir: String? = "C:\\\\plugins"

    lateinit var plugin: PluginEntity
    lateinit var pluginHistory: PluginHistoryEntity
    lateinit var pluginParam: PluginParamDto
    lateinit var tokenDto: WfTokenDto
    protected var body: String? = null

    /**
     * 공통 초기화 (공통 처리 후 각 세부 설정 호출)
     */
    fun initialize(
        plugin: PluginEntity,
        tokenDto: WfTokenDto,
        body: String?
    ) {
        this.plugin = plugin
        this.tokenDto = tokenDto
        this.pluginParam = this.initPluginParam()
        this.body = body
        val pluginHistory = pluginHistoryRepository.save(
            PluginHistoryEntity(
                historyId = "",
                pluginId = plugin.pluginId,
                startDt = LocalDateTime.now()
            )
        )
        this.pluginHistory = pluginHistory
        this.constructor()
        pluginHistoryRepository.save(pluginHistory)
    }

    /**
     * 컴포넌트별 초기화
     */
    abstract fun constructor()

    /**
     * 플러그인 실행
     */
    abstract fun execute(param: LinkedHashMap<String, Any>): ZResponse

    /**
     * 실행 명령어 생성
     */
    protected fun getCommand(options: List<String>): MutableList<String> {
        val home = File(this.pluginsDir + this.plugin.pluginLocation).absolutePath
        val commands = plugin.pluginCommand.split(" ")
        val command = mutableListOf<String>()
        commands.forEach {
            if (it == AliceConstants.SCHEDULER_COMMAND_JAR) {
                command.add(AliceConstants.PLUGINS_VM_OPTIONS_LOG_CONFIG_FILE + "=" + home + "/logback.xml")
                command.add(AliceConstants.PLUGINS_VM_OPTIONS_LOG_HOME + "=" + home + "/logs")
                command.add(AliceConstants.PLUGINS_VM_OPTIONS_ENCODING + "=" + "UTF-8")
            }
            command.add(it.trim())
        }
        command.add(this.pluginHistory.historyId)
        options.forEach { command.add(it) }
        return command
    }

    /**
     * ProcessBuilder 생성
     */
    protected fun getProcessBuilder(command: MutableList<String>): ProcessBuilder {
        val processBuilder = ProcessBuilder(command)
        processBuilder.directory(File(this.pluginsDir + this.plugin.pluginLocation))
        processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT)
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
        return processBuilder
    }

    /**
     * jar 파일 실행
     */
    protected fun executeJar(options: List<String>): ZResponse {
        var errorMsg = ""
        var isSuccess = true
        val command = this.getCommand(options)
        try {
            val runnable = Runnable {
                val process = this.getProcessBuilder(command).start()
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
            logger.error(errorMsg)
        }

        val resultMap = LinkedHashMap<String, Boolean>()
        resultMap["result"] = isSuccess
        return ZResponse(
            message = errorMsg,
            data = resultMap
        )
    }

    /**
     * PluginData 를 Tag 정보로 조회하여 처리
     */
    protected fun getPluginDataByTag(): LinkedHashMap<String, String> {
        val dataMap: LinkedHashMap<String, String> = linkedMapOf()
        val tokenDataList = wfTokenDataRepository.findWfTokenDataEntitiesByTokenTokenId(this.tokenDto.tokenId)
        val componentIds: LinkedHashSet<String> = linkedSetOf()
        tokenDataList.forEach {
            componentIds.add(it.component.componentId)
        }
        val tagData = aliceTagRepository.findByTargetIds(AliceTagConstants.TagType.COMPONENT.code, componentIds)
        tokenDataList.forEach { tokenData ->
            tagData.forEach { tag ->
                if (tag.targetId == tokenData.component.componentId) {
                    dataMap[tag.tagValue] = tokenData.value
                }
            }
        }
        return dataMap
    }

    fun initPluginParam(): PluginParamDto {
        return PluginParamDto(
            tokenId = this.tokenDto.tokenId
        )
    }
}
