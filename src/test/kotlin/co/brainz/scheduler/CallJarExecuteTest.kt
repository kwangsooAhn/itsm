/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.scheduler

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Call Jar File")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CallJarExecuteTest {

    @Test
    @DisplayName("Run Jar File")
    fun runJarFile() {
        val pluginsHome = "plugins"
        val jarHome = "zeniusEms"
        val jarDir = pluginsHome + File.separator + jarHome
        val jarFile = "string.encryptor_2.0.jar"
        val key = "itsm"
        val value = "itsm123"

        if (File(jarDir).exists() && File(jarDir + File.separator + jarFile).exists()) {
            // command list
            val command = mutableListOf<String>()
            command.add("java")
            command.add("-jar")
            command.add(jarFile)
            command.add(value)
            command.add(key)
            val processBuilder = ProcessBuilder(command)
            processBuilder.directory(File(jarDir))
            val process = processBuilder.start()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println(line)
            }
        }
    }
}
