/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.scheduling.task

import co.brainz.framework.configuration.AliceApplicationContextProvider
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.fileTransaction.repository.AliceFileLocRepository
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalDateTime
import java.time.ZoneId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

/**
 * 주기적으로 임시 업로드된 파일을 삭제하는 클래스
 */
@Component
class DeleteTempFile() : Runnable {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    lateinit var fileRootDir: String
    private val tempDirectoryName = "temp"
    private val oneDayAgo = LocalDateTime.now(ZoneId.of("UTC")).minusDays(1)

    /**
     * 임시 업로드된 파일을 삭제한다.
     * /root/avatar/temp
     * /root/temp/
     */
    override fun run() {
        val appContext = AliceApplicationContextProvider.getApplicationContext()
        val environment = appContext.getBean(Environment::class.java)
        val aliceFileLocRepository = appContext.getBean(AliceFileLocRepository::class.java)
        fileRootDir = environment.getProperty("file.upload.dir").toString()

        // 아바타 temp 삭제
        val avatarTempDir = fileRootDir + File.separator + AliceUserConstants.AVATAR_IMAGE_TEMP_DIR
        val avatarFiles = Paths.get(avatarTempDir).toFile().listFiles()
        deleteFiles(avatarFiles)

        // 일반 temp 삭제
        val tempDir = fileRootDir + File.separator + tempDirectoryName
        val tempFiles = Paths.get(tempDir).toFile().listFiles()
        deleteFiles(tempFiles)

        // db file_loc 삭제
        val filesToDelete = aliceFileLocRepository.findByUploadedAndCreateDtLessThan(false, this.oneDayAgo)
        aliceFileLocRepository.deleteInBatch(filesToDelete)

        logger.info("End delete temporary files")
    }

    /**
     * 파일을 물리적으로 삭제한다.
     */
    private fun deleteFiles(files: Array<File>?) {
        if (files !== null) {
            for (file in files) {
                confirmAndDelete(file)
            }
        }
    }

    /**
     * 파일 종류에 따라 재귀호출 또는 삭제
     */
    private fun confirmAndDelete(file: File) {
        val filePath = file.toPath()
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files !== null) {
                this.deleteFiles(files)
            }
            Files.deleteIfExists(filePath)
        } else {
            val attribute = Files.readAttributes(filePath, BasicFileAttributes::class.java)
            val fileDateTime = LocalDateTime.ofInstant(attribute.creationTime().toInstant(), ZoneId.of("UTC"))
            val passedOneDay = fileDateTime < this.oneDayAgo
            if (passedOneDay) {
                Files.deleteIfExists(filePath)
            }
        }
    }
}
