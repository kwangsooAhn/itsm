/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.util

import co.brainz.framework.resourceManager.constants.ResourceConstants
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
import org.springframework.web.multipart.MultipartFile

open class AliceResourceUtil(
    private val environment: Environment
) {

    @Value("\${file.upload.dir}")
    lateinit var basePath: String

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
                scaledWidth = ResourceConstants.THUMBNAIL_WIDTH_ICON
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
                scaledWidth = ResourceConstants.THUMBNAIL_WIDTH
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
     * 파일 경로 반환
     * 이름이 동일할 경우 파일명(1) 와 같이 처리한다.
     */
    fun getUploadFileWithModifyName(path: String, file: MultipartFile): File {
        var modifyPath = Paths.get(path + File.separator + file.originalFilename)
        var modifyFile = modifyPath.toFile()
        val oriName = modifyFile.nameWithoutExtension
        var num = 1
        var modifyName: String
        // 기존 경로가 존재할 경우
        while (modifyFile.exists()) {
            modifyName = oriName + "(" + num++ + ")." + modifyFile.extension
            modifyPath = Paths.get(path + File.separator + modifyName)
            modifyFile = modifyPath.toFile()
        }
        return modifyFile
    }

    /**
     * 업로드 대상 파일 경로 구하기 - 현재날짜로 폴더가 생성됨
     *
     * @param root 업로드할 경로
     * @param fileName 업로드할 파일명
     */
    open fun getUploadFilePathWithNow(root: String, fileName: String?): Path {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat(ResourceConstants.FILE_NAME_FORMAT)
        var dir = Paths.get(this.getPath(root).toString() + File.separator + df.format(cal.time))
        dir = if (Files.exists(dir)) dir else Files.createDirectories(dir)
        return Paths.get(dir.toString() + File.separator + fileName)
    }

    /**
     * 허용된 이미지 확장자 목록 조회
     */
    open fun getImageExtensions(): List<String> {
        return ResourceConstants.IMAGE_EXTENSIONS
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
     * 타입에 따라 이미지 파일만 가져올지 여부를 판단
     * @param type 타입
     */
    fun isAllowedOnlyImageByType(type: String): Boolean {
        return when (type) {
            ResourceConstants.FileType.ICON.code,
            ResourceConstants.FileType.CI_ICON.code,
            ResourceConstants.FileType.IMAGE.code,
            ResourceConstants.FileType.PROCESS.code,
            ResourceConstants.FileType.AVATAR.code -> true
            else -> false
        }
    }

    /**
     * 이미지 파일 여부
     * @param extension 확장자
     */
    fun isImage(extension: String): Boolean {
        return ResourceConstants.IMAGE_EXTENSIONS.indexOf(extension.toUpperCase()) > -1
    }

    /**
     * 파일명 | 폴더명이 검색 조건에 부합하는지 여부
     * @param name 파일명 | 폴더명
     * @param matchValue 검색어
     */
    fun isMatchedInSearch(name: String, matchValue: String): Boolean {
        if (matchValue.isEmpty()) { return true }

        return name.toLowerCase().matches(".*${matchValue.toLowerCase()}.*".toRegex())
    }

    /**
     * 폴더 사이즈 구하기
     * @param dir 폴더
     */
    fun getFolderSize(dir: File, depth: Int): Long {
        return dir.walkTopDown().maxDepth(depth).filter { it.isFile }.map { it.length() }.sum()
    }
    /**
     * 폴더 사이즈 구하기
     * @param dir 폴더
     */
    fun getImageFolderSize(dir: File, depth: Int): Long {
        return dir.walkTopDown().maxDepth(depth).filter { it.isFile && isImage(it.extension) }.map { it.length() }.sum()
    }

    /**
     * 조건과 일치하는 폴더, 파일 갯수 구하기 (자기 자신 제외)
     * @param dir 폴더
     */
    fun getExternalPathCount(dir: File, matchValue: String, depth: Int): Int {
        val count = dir.walk().maxDepth(depth)
            .filter { isMatchedInSearch(it.name, matchValue) }
            .count()
        return if (count > 0) { count - 1 } else { 0 }
    }

    /**
     * 조건과 일치하는 폴더, 이미지 파일 갯수 구하기 (자기 자신 제외)
     */
    fun getExternalPathImageCount(dir: File, matchValue: String, depth: Int): Int {
        val count = dir.walk().maxDepth(depth)
            .filter { isMatchedInSearch(it.name, matchValue) && (it.isDirectory || isImage(it.extension)) }
            .count()
        return if (count > 0) { count - 1 } else { 0 }
    }
}
