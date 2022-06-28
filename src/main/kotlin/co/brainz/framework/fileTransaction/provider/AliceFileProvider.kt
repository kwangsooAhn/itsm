/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.fileTransaction.provider

import co.brainz.framework.fileTransaction.constants.FileConstants
import co.brainz.framework.fileTransaction.dto.AliceFileDetailDto
import co.brainz.framework.fileTransaction.dto.AliceFileDetailListReturnDto
import co.brainz.framework.fileTransaction.entity.AliceFileNameExtensionEntity
import co.brainz.framework.fileTransaction.repository.AliceFileNameExtensionRepository
import co.brainz.framework.util.AliceFileUtil
import co.brainz.itsm.constants.ItsmConstants
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.stream.Collectors
import javax.imageio.ImageIO
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class AliceFileProvider(
    private val aliceFileNameExtensionRepository: AliceFileNameExtensionRepository,
    environment: Environment
) : AliceFileUtil(environment) {

    private val allowedImageExtensions = listOf("png", "gif", "jpg", "jpeg")
    private val classPathInJar = "/BOOT-INF/classes/"

    /**
     * 프로세스 상태 파일 로드
     */
    fun getProcessStatusFile(processId: String): File {
        val dir = super.getPath(FileConstants.Path.PROCESSES.path)
        val filePath = Paths.get(dir.toString() + File.separator + processId + ".xml")
        return filePath.toFile()
    }

    /**
     * 허용된 이미지 확장자 목록 조회
     */
    fun getAllowedImageExtensions(): List<String> {
        return this.allowedImageExtensions
    }

    /**
     * 파일 허용 확장자 목록 가져오기
     */
    fun getFileNameExtension(): List<AliceFileNameExtensionEntity> {
        return aliceFileNameExtensionRepository.findAll()
    }

    /**
     * 내부 경로에 있는 이미지 경로 리스트 가져오기
     *
     */
    // 2021-03-25 Jung Hee Chan
    // 기존에 CI Type 및 신청서용 아이콘이 Jar 파일 내부에 포함되어 있어서 만들었음.
    // 현재 사용되는 곳이 없으나 남겨둠.
    fun getInternalImageDataList(
        type: String,
        dir: String,
        searchValue: String,
        currentOffset: Int
    ): AliceFileDetailListReturnDto {
        var fileList = mutableListOf<Path>()
        val resourceURL = javaClass.classLoader.getResource(dir)

        // 2021-03-17 Jung Hee Chan
        // 내부 경로에 위치한 이미지 파일은 경우 Jar 파일 내부에 있을 수 있어 파일시스템을 이용해서 Path 리스트를 취득.
        resourceURL?.let {
            FileSystems.newFileSystem(
                resourceURL.toURI(),
                mutableMapOf<String, String>()
            ).use { fs ->
                fileList = Files.walk(fs.getPath(classPathInJar + dir))
                    .filter { path: Path ->
                        val fileName = path.fileName.toString()

                        Files.isRegularFile(path) && // 파일 타입만 조회
                                // 허용 확장자 체크
                                (allowedImageExtensions.indexOf(
                                    fileName.substring(
                                        (fileName.lastIndexOf(".") + 1),
                                        fileName.length
                                    ).toLowerCase()
                                ) > -1) &&
                                fileName.matches(".*$searchValue.*".toRegex()) // 검색조건 적용
                    }
                    .collect(Collectors.toList())
            }
        }

        var startIndex = 0
        if (currentOffset != -1) { // -1인 경우는 전체 조회
            startIndex = currentOffset
        }

        // 2021-03-17 Jung Hee Chan
        // 내부 경로에 위치한 이미지 파일은 Jar 파일 내부에 있을 수 있어 File 이 아닌 Stream 형태로 읽어옴.
        // 이때 기존 File 방식과 달리 확장자, 사이즈, 수정일자등 데이터를 가져올 수 없음. --> 이미지 관리에 대한 개선의 여지가 있음.
        val imageList = mutableListOf<AliceFileDetailDto>()
        for (i in startIndex until getImageListEndIndex(currentOffset, fileList.size)) {
            var filePathInJAR = fileList[i].toString()
            val fileName = fileList[i].fileName.toString()

            // 윈도우즈 환경인 경우 제일 앞 / 제거가 필요.
            if (filePathInJAR.startsWith("/")) {
                filePathInJAR = filePathInJAR.substring(1, filePathInJAR.length)
            }

            val fileInputStream = javaClass.classLoader.getResourceAsStream(filePathInJAR)
            val bufferedImage = ImageIO.read(fileInputStream)
            val resizedBufferedImage = resizeBufferedImage(bufferedImage, type)
            imageList.add(
                AliceFileDetailDto(
                    name = fileName,
                    extension = "",
                    fullpath = "",
                    size = "",
                    data = super.encodeToString(
                        resizedBufferedImage,
                        fileName.substring((fileName.lastIndexOf(".") + 1), fileName.length).toLowerCase()
                    ),
                    width = bufferedImage.width,
                    height = bufferedImage.height,
                    updateDt = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(0),
                        ZoneId.systemDefault()
                    )
                )
            )
        }
        return AliceFileDetailListReturnDto(
            data = imageList,
            totalCount = fileList.size.toLong()
        )
    }

    /**
     * 외부 경로에 있는 파일의 정보를 조회
     */
    fun getExternalFileList(
        type: String,
        searchValue: String,
        currentOffset: Int = -1
    ): AliceFileDetailListReturnDto {
        val dir = getExternalDir(type)
        val dirPath = super.getPath(dir)
        val fileList = this.getValidFileList(type, dirPath, searchValue)
        val dataList = mutableListOf<AliceFileDetailDto>()
        var startIndex = 0
        if (currentOffset != -1) { // -1인 경우는 전체 조회
            startIndex = currentOffset
        }
        for (i in startIndex until getImageListEndIndex(currentOffset, fileList.size)) {
            val file = fileList[i].toFile()
            if (allowedImageExtensions.indexOf(file.extension.toLowerCase()) > -1) {
                val bufferedImage = ImageIO.read(file)
                val resizedBufferedImage = resizeBufferedImage(bufferedImage, type)
                dataList.add(
                    AliceFileDetailDto(
                        name = file.name,
                        extension = file.extension,
                        fullpath = file.absolutePath,
                        size = super.humanReadableByteCount(file.length()),
                        data = super.encodeToString(resizedBufferedImage, file.extension),
                        width = bufferedImage.width,
                        height = bufferedImage.height,
                        updateDt = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(file.lastModified()),
                            ZoneId.systemDefault()
                        )
                    )
                )
            } else {
                dataList.add(
                    AliceFileDetailDto(
                        name = file.name,
                        extension = file.extension,
                        fullpath = file.absolutePath,
                        size = super.humanReadableByteCount(file.length()),
                        updateDt = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(file.lastModified()),
                            ZoneId.systemDefault()
                        )
                    )
                )
            }
        }
        return AliceFileDetailListReturnDto(
            data = dataList,
            totalCount = fileList.size.toLong(),
            totalCountWithoutCondition = getFileTotalCount(dirPath)
        )
    }

    /**
     * 외부 경로 조회
     */
    fun getExternalDir(type: String): String {
        return when (type) {
            FileConstants.Type.ICON.code -> FileConstants.Path.ICON_DOCUMENT.path
            FileConstants.Type.ICON_CI_TYPE.code -> FileConstants.Path.ICON_CI_TYPE.path
            else -> FileConstants.Path.FILE.path
        }
    }

    /**
     * 외부경로의 이미지 파일을 데이터로 읽기.
     *
     * @param fileFullName 파일 경로와 파일명까지 포함된 전체 이름
     * @return String 화면 태그에서 사용할 수 있는 형태의 data URI Schema
     */
    fun getDataUriSchema(fileFullName: String): String {
        return "data:image/png;base64," + super.getImageData(fileFullName)
    }

    /**
     * [type] 에 따라 파일을 조회하여 목록을 생성 (검색조건 포함)
     *  - type: file 모든 파일
     *  - image, icon, cmdb-icon: filter 된 이미지 파일
     */
    fun getValidFileList(type: String, dirPath: Path, searchValue: String): List<Path> {
        val fileList = mutableListOf<Path>()
        if (Files.isDirectory(dirPath)) {
            val fileDirMap = Files.list(dirPath).collect(Collectors.partitioningBy { Files.isDirectory(it) })
            fileDirMap[false]?.forEach { filePath ->
                val file = filePath.toFile()
                when (type) {
                    FileConstants.Type.ICON.code,
                    FileConstants.Type.ICON_CI_TYPE.code,
                    FileConstants.Type.IMAGE.code -> {
                        if (allowedImageExtensions.indexOf(file.extension.toLowerCase()) > -1) {
                            if (this.searchValueValid(file, searchValue)) {
                                fileList.add(filePath)
                            }
                        }
                    }
                    else -> {
                        if (this.searchValueValid(file, searchValue)) {
                            fileList.add(filePath)
                        }
                    }
                }
            }
        }
        return fileList
    }

    /**
     * 검색 조건 필터링
     */
    private fun searchValueValid(file: File, searchValue: String): Boolean {
        var isValid = false
        val lowerValue = searchValue.toLowerCase()
        if (searchValue.isNotEmpty()) {
            if (file.name.toLowerCase().matches(".*$lowerValue.*".toRegex())) {
                isValid = true
            }
        } else {
            isValid = true
        }
        return isValid
    }

    /**
     * 파일 전체 건수
     */
    fun getFileTotalCount(dirPath: Path): Long {
        var totalCount = 0L
        if (Files.isDirectory(dirPath)) {
            totalCount = Files.list(dirPath).count()
        }
        return totalCount
    }

    /**
     * 조회하는 이미지 리스트의 갯수를 계산
     */
    fun getImageListEndIndex(currentOffset: Int, maxSize: Int): Int {
        var endIndex: Int
        // currentOffset 값이 현재 인덱스의 값을 나타내면서 전체 조회인지 여부까지 포함해서 이중적으로 사용되고 있음.
        if (currentOffset == -1) {
            endIndex = maxSize // 전체 목록 조회인 경우
        } else {
            endIndex = currentOffset + ItsmConstants.IMAGE_OFFSET_COUNT
            if (maxSize < endIndex) endIndex = maxSize
        }
        return endIndex
    }
}
