/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.cmdb.ciIcon.service

import co.brainz.framework.resourceManager.constants.ResourceConstants
import co.brainz.framework.resourceManager.provider.AliceResourceProvider
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceResourceUtil
import co.brainz.itsm.cmdb.ciIcon.dto.CIIconDto
import co.brainz.itsm.cmdb.ciIcon.dto.CIIconListReturnDto
import co.brainz.itsm.cmdb.ciIcon.entity.CIIconEntity
import co.brainz.itsm.cmdb.ciIcon.repository.CIIconRepository
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional
class CIIconService(
    private val ciIconRepo: CIIconRepository,
    private val resourceProvider: AliceResourceProvider,
    environment: Environment
): AliceResourceUtil(environment) {

    /**
     * CMDB CI Icon 목록 조회
     * @param search 검색어
     * @param offset -1일 경우 전체 조회
     */
    fun getCIIcons(search: String, offset: Int): CIIconListReturnDto {
        val type = ResourceConstants.FileType.CI_ICON.code
        val dirPath = resourceProvider.getExternalPath(type)
        val fileList = resourceProvider.getValidFileList(type, dirPath, search)
        val iconList = ciIconRepo.findAll()
        val dataList = mutableListOf<CIIconDto>()
        var startIndex = 0
        if (offset != -1) { startIndex = offset }

        val extList = resourceProvider.getAllowedExtensions(type)
        for (i in startIndex until resourceProvider.getImageListEndIndex(offset, fileList.size)) {
            val file = fileList[i].toFile()
            // 이미지 파일만 업로드
            if (extList.contains(file.extension.toLowerCase())) {
                val icon = iconList.firstOrNull { it.fileName == file.nameWithoutExtension && it.fileNameExtension == file.extension }
                val bufferedImage = ImageIO.read(file)
                val resizedBufferedImage = resizeBufferedImage(bufferedImage, type)
                dataList.add(
                    CIIconDto(
                        name = file.name,
                        extension = file.extension,
                        fullpath = file.absolutePath,
                        editable = icon?.editable ?: true,
                        size = super.humanReadableByteCount(file.length()),
                        data = super.encodeToString(resizedBufferedImage, file.extension),
                        width = bufferedImage.width,
                        height = bufferedImage.height,
                        updateDt = icon?.updateDt.let { icon?.createDt }
                    )
                )
            }
        }

        return CIIconListReturnDto(
            data = dataList,
            totalCount = fileList.size.toLong(),
            totalCountWithoutCondition = resourceProvider.getFileTotalCount(dirPath)
        )
    }

    /**
     * CMDB CI Icon 업로드
     * @param multipartFiles 파일목록
     */
    fun uploadCIIcons(multipartFiles: List<MultipartFile>): ZResponse {
        val type = ResourceConstants.FileType.CI_ICON.code
        var status = ZResponseConstants.STATUS.SUCCESS
        val dirPath = resourceProvider.getExternalPath(type)
        val extList = resourceProvider.getAllowedExtensions(type)
        try {
            multipartFiles.forEach {
                var filePath = Paths.get(dirPath.toString() + File.separator + it.originalFilename)
                var file = filePath.toFile()
                // 이미지 확장자 체크
                if (extList.contains(file.extension.toLowerCase())) {
                    val oriFileNameWithoutExtension = file.nameWithoutExtension
                    var num = 1
                    var fileName: String
                    while (file.exists()) { // 동일한 파일이 존재하는 경우
                        fileName = oriFileNameWithoutExtension + "(" + num++ + ")." + file.extension

                        // 현재 올라간 파일의 파일명을 변경한다.
                        filePath = Paths.get(dirPath.toString() + File.separator + fileName)
                        file = filePath.toFile()
                    }
                    it.transferTo(file)

                    // 저장
                    val iconFileEntity = CIIconEntity(
                        fileSeq = 0,
                        fileName = file.nameWithoutExtension,
                        fileNameExtension = file.extension,
                        editable = true
                    )
                    ciIconRepo.save(iconFileEntity)
                }
            }
        } catch (e: Exception) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * CMDB CI Icon 파일명 수정
     */
    fun renameCIIcon(originName: String, modifyName: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val dirPath = resourceProvider.getExternalPath(ResourceConstants.FileType.CI_ICON.code)
        val filePath = Paths.get(dirPath.toString() + File.separator + originName)
        val file = filePath.toFile()
        val modifyFile = File(dirPath.toFile(), modifyName)

        // 파일이 존재하지 않는다면 즉시 에러 반환
        if (!file.exists() || modifyFile.exists()) {
            return ZResponse(
                status = ZResponseConstants.STATUS.ERROR_NOT_EXIST.code
            )
        }

        // 파일명 수정
        try {
            file.renameTo(modifyFile)

            val ciIcon = ciIconRepo.findByFileNameAndFileNameExtension(file.nameWithoutExtension, file.extension)
            ciIcon.fileName = modifyFile.nameWithoutExtension
            ciIconRepo.save(ciIcon)
        } catch (e: IOException) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * CMDB CI Icon 파일 삭제
     */
    fun deleteCIIcon(name: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val dirPath = resourceProvider.getExternalPath(ResourceConstants.FileType.CI_ICON.code)
        val filePath = Paths.get(dirPath.toString() + File.separator + name)
        try {
            Files.delete(filePath)

            val fileNames = name.split('.')
            val ciIcon = ciIconRepo.findByFileNameAndFileNameExtension(fileNames[0], fileNames[1])
            ciIconRepo.deleteById(ciIcon.fileSeq)
        } catch (e: IOException) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
            e.printStackTrace()
        }

        return ZResponse(
            status = status.code
        )
    }
}
