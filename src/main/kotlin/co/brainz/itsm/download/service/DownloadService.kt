/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.download.service

import co.brainz.framework.fileTransaction.dto.AliceFileDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.download.dto.DownloadDto
import co.brainz.itsm.download.dto.DownloadSearchDto
import co.brainz.itsm.download.entity.DownloadEntity
import co.brainz.itsm.download.mapper.DownloadMapper
import co.brainz.itsm.download.repository.DownloadRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.transaction.Transactional
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class DownloadService(
    private val downloadRepository: DownloadRepository,
    private val aliceFileService: AliceFileService
) {

    private val downloadMapper: DownloadMapper = Mappers.getMapper(DownloadMapper::class.java)

    /**
     * 자료실 목록 조회.
     *
     * @param downloadSearchDto
     * @return List<DownloadDto>
     */
    fun getDownloadList(downloadSearchDto: DownloadSearchDto): List<DownloadDto> {
        val fromDt = LocalDateTime.parse(downloadSearchDto.fromDt, DateTimeFormatter.ISO_DATE_TIME)
        val toDt = LocalDateTime.parse(downloadSearchDto.toDt, DateTimeFormatter.ISO_DATE_TIME)
        val offset = downloadSearchDto.offset
        val queryResult = when (downloadSearchDto.category) {
            "all" -> downloadRepository.findDownloadEntityList(
                "",
                downloadSearchDto.search,
                fromDt,
                toDt,
                offset
            )
            else -> downloadRepository.findDownloadEntityList(
                downloadSearchDto.category,
                downloadSearchDto.search,
                fromDt,
                toDt,
                offset
            )
        }
        val downloadList = mutableListOf<DownloadDto>()
        for (download in queryResult.results) {
            val downloadData = downloadMapper.toDownloadDto(download)
            downloadData.totalCount = queryResult.total
            downloadList.add(downloadData)
        }
        return downloadList
    }

    /**
     * 자료실 저장.
     *
     * @param downloadDto
     */
    @Transactional
    fun saveDownload(downloadDto: DownloadDto) {
        val downloadEntity = downloadRepository.save(downloadMapper.toDownloadEntity(downloadDto))
        aliceFileService.upload(
            AliceFileDto(
                ownId = downloadEntity.downloadId,
                fileSeq = downloadDto.fileSeqList,
                delFileSeq = downloadDto.delFileSeqList
            )
        )
    }

    /**
     * 자료실 상세 조회.
     *
     * @param downloadId
     * @param type
     * @return DownloadDto
     */
    @Transactional
    fun getDownload(downloadId: String, type: String): DownloadDto {
        var downloadEntity = downloadRepository.findById(downloadId).orElse(DownloadEntity())
        if (type === "view") {
            downloadEntity.views += 1
            downloadEntity = downloadRepository.save(downloadEntity)
        }
        return downloadMapper.toDownloadDto(downloadEntity)
    }

    /**
     * 자료실 삭제.
     *
     * @param downloadId
     */
    @Transactional
    fun deleteDownload(downloadId: String) {
        downloadRepository.deleteById(downloadId)
        aliceFileService.delete(downloadId)
    }
}
