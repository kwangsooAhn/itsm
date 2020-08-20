/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.download.service

import co.brainz.framework.fileTransaction.dto.AliceFileDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.download.dto.DownloadDto
import co.brainz.itsm.download.dto.DownloadListDto
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
     * [downloadSearchDto]를 받아서 자료실 목록를 [List<DownloadListDto>] 반환한다.
     *
     */
    fun getDownloadList(downloadSearchDto: DownloadSearchDto): List<DownloadListDto> {
        val fromDt = LocalDateTime.parse(downloadSearchDto.fromDt, DateTimeFormatter.ISO_DATE_TIME)
        val toDt = LocalDateTime.parse(downloadSearchDto.toDt, DateTimeFormatter.ISO_DATE_TIME)
        val offset = downloadSearchDto.offset

        return downloadRepository.findDownloadEntityList(
            downloadSearchDto.category,
            downloadSearchDto.search,
            fromDt,
            toDt,
            offset
        )
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
