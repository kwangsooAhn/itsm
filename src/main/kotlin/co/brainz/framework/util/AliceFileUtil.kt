package co.brainz.framework.util

import co.brainz.framework.constants.AliceConstants
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

    private val thumbnailImageWidth = 300
    private val thumbnailIconWidth = 100

    /**
     * 파일명으로 사용할 값 리턴 (난수화)
     */
    fun getRandomFilename(): String {
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
        return java.lang.String.format("%.1f %cByte", value / 1024.0, ci.current())
    }

    /**
     * 이미지 사이즈 조정.
     */
    fun resizeBufferedImage(image: BufferedImage, type: String): BufferedImage {
        var scaledWidth = 0
        var scaledHeight = 0
        when (type) {
            AliceConstants.FileType.ICON.code -> {
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
                scaledHeight = image.height / (image.width / scaledWidth)
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
     * @param rootDir 업로드할 경로
     * @param fileName 업로드할 파일명
     */
    fun getDir(rootDir: String, fileName: String?): Path {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyyMMdd")

        if (basePath == "") {
            this.basePath = environment.getProperty("catalina.base").toString()
        }

        var dir: Path = Paths.get(basePath + File.separator + rootDir + File.separator + df.format(cal.time))
        dir = if (Files.exists(dir)) dir else Files.createDirectories(dir)
        return Paths.get(dir.toString() + File.separator + fileName)
    }

    /**
     * 프로세스 및 이미지관리에서 사용하는 dir 조회.
     */
    fun getWorkflowDir(rootDir: String): Path {
        if (this.basePath == "") {
            this.basePath = environment.getProperty("catalina.base").toString()
        }

        var dir: Path = Paths.get(basePath + File.separator + rootDir)
        dir = if (Files.exists(dir)) dir else Files.createDirectories(dir)
        return dir
    }
}
