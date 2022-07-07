/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.util

import co.brainz.framework.fileTransaction.constants.ResourceConstants
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.text.StringCharacterIterator
import java.util.Base64
import java.util.Calendar
import javax.imageio.ImageIO
import kotlin.math.abs
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment

open class AliceFileUtil(
    private val environment: Environment
) {

    @Value("\${file.upload.dir}")
    lateinit var basePath: String

    private val imageExtensions = listOf("png", "gif", "jpg", "jpeg")
    private val thumbnailImageWidth = 700
    private val thumbnailIconWidth = 100

    /**
     * 파일명으로 사용할 값 리턴 (난수화)
     */
    open fun getRandomFilename(): String {
        var charCode: Char
        var fileName = ""
        for (i in 0 until 12) {
            charCode = (Math.random() * 26 + 65).toChar()
            fileName += charCode
        }
        return fileName
    }

    /**
     * 이미지 data 조회.
     */
    fun encodeToString(image: BufferedImage, type: String): String {
        var imageString = ""
        val bos = ByteArrayOutputStream()
        try {
            ImageIO.write(image, type, bos)
            val imageBytes = bos.toByteArray()
            imageString = Base64.getEncoder().encodeToString(imageBytes)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            bos.close()
        }
        return imageString
    }

    /**
     * byte 사이즈를 단위를 넣어 변환한다.
     */
    fun humanReadableByteCount(bytes: Long): String {
        val absB = if (bytes == Long.MIN_VALUE) Long.MAX_VALUE else abs(bytes)
        if (absB < 1024) {
            return "$bytes Byte"
        }
        var value = absB
        val ci = StringCharacterIterator("KMGTPE")
        var i = 40
        while (i >= 0 && absB > 0xfffccccccccccccL shr i) {
            value = value shr 10
            ci.next()
            i -= 10
        }
        value *= java.lang.Long.signum(bytes)
        return java.lang.String.format("%.1f %cB", value / 1024.0, ci.current())
    }

    /**
     * 이미지 사이즈 조정.
     */
    fun resizeBufferedImage(image: BufferedImage, type: String): BufferedImage {
        var scaledWidth: Int
        var scaledHeight: Int
        when (type) {
            ResourceConstants.FileType.ICON.code -> {
                scaledWidth = this.thumbnailIconWidth
                if (image.width < scaledWidth) {
                    scaledWidth = image.width
                }
                scaledHeight = image.height / (image.width / scaledWidth)

                // 이미지 비율 조정 (아이콘은 가로, 세로 크기가 동일하게 맞춘다.)
                when {
                    scaledWidth < scaledHeight -> {
                        val ratio = scaledHeight / scaledWidth.toDouble()
                        scaledWidth = (scaledWidth * ratio).toInt()
                    }
                    scaledWidth > scaledHeight -> {
                        val ratio = scaledWidth / scaledHeight.toDouble()
                        scaledHeight = (scaledHeight * ratio).toInt()
                    }
                }
            }
            else -> {
                scaledWidth = this.thumbnailImageWidth
                if (image.width < scaledWidth) {
                    scaledWidth = image.width
                }
                scaledHeight = image.height * scaledWidth / image.width
            }
        }

        val bufferedImage = BufferedImage(scaledWidth, scaledHeight, image.type)
        val g2d = bufferedImage.createGraphics()
        g2d.drawImage(image, 0, 0, scaledWidth, scaledHeight, null)
        g2d.dispose()
        return bufferedImage
    }

    /**
     * 업로드 대상 파일 경로 구하기
     *
     * @param root 업로드할 경로
     * @param fileName 업로드할 파일명
     */
    open fun getUploadFilePath(root: String, fileName: String?): Path {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyyMMdd")
        var dir = Paths.get(this.getPath(root).toString() + File.separator + df.format(cal.time))
        dir = if (Files.exists(dir)) dir else Files.createDirectories(dir)
        return Paths.get(dir.toString() + File.separator + fileName)
    }

    /**
     * 외부경로의 이미지 파일을 데이터로 읽기.
     *
     * @param fileFullName 파일 경로와 파일명까지 포함된 전체 이름
     * @return String base64로 인코딩된 데이터 스트링
     */
    fun getImageData(fileFullName: String): String {
        val imageData: String
        if (this.basePath == "") {
            this.basePath = environment.getProperty("catalina.base").toString()
        }

        val file = Paths.get(basePath + File.separator + fileFullName).toFile()
        imageData = if (file.exists()) {
            val bufferedImage = ImageIO.read(file)
            encodeToString(bufferedImage, file.extension)
        } else {
            ""
        }
        return imageData
    }

    //////////////////////////////// 여기서 부터 우다정이 만든거 ///////////////////////
    /**
     * 경로 구하기 (Root)
     * @param path 하위경로
     */
    fun getPath(path: String): Path {
        if (this.basePath == "") {
            this.basePath = environment.getProperty("catalina.base").toString()
        }
        var dir: Path = Paths.get(basePath + File.separator + path)
        dir = if (Files.exists(dir)) dir else Files.createDirectories(dir)
        return dir
    }

    /**
     * 외부 경로 조회
     * @param type 타입
     */
    fun getExternalPath(type: String): Path {
        val dir = when (type) {
            ResourceConstants.FileType.ICON.code -> ResourceConstants.Path.ICON_DOCUMENT.path
            ResourceConstants.FileType.CI_ICON.code -> ResourceConstants.Path.ICON_CI_TYPE.path
            else -> ResourceConstants.Path.FILE.path
        }
        return getPath(dir)
    }

    /**
     * 타입에 따라 이미지 파일만 가져올지 여부를 판단
     * @param type 타입
     */
    fun isAllowedOnlyImageByType(type: String): Boolean {
        return when(type) {
            ResourceConstants.FileType.ICON.code,
            ResourceConstants.FileType.CI_ICON.code,
            ResourceConstants.FileType.IMAGE.code -> true
            else -> false
        }
    }

    /**
     * 이미지 파일 여부
     * @param extension 확장자
     */
    fun isImage(extension: String): Boolean {
        return imageExtensions.indexOf(extension.toLowerCase()) > -1
    }

    /**
     * 파일명 | 폴더명이 검색 조건에 부합하는지 여부
     * @param name 파일명 | 폴더명
     * @param matchValue 검색어
     */
    fun isMatchedInSearch(name: String, matchValue: String): Boolean {
        if (matchValue.isEmpty())  { return true }

        return name.toLowerCase().matches(".*${matchValue.toLowerCase()}.*".toRegex())
    }
}
