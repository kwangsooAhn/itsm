/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.service

import co.brainz.framework.fileTransaction.dto.AliceFileDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.faq.dto.FaqDto
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.dto.FaqSearchRequestDto
import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.faq.mapper.FaqMapper
import co.brainz.itsm.faq.repository.FaqRepository
import org.mapstruct.factory.Mappers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * ### FAQ 관련 서비스 레이어 클래스.
 *
 * FAQ관련 데이터 조회/등록/변경/삭제를 처리한다.
 *
 * @author Jung heechan
 */
@Service
class FaqService(private val faqRepository: FaqRepository, private val aliceFileService: AliceFileService) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val faqMapper: FaqMapper = Mappers.getMapper(FaqMapper::class.java)

    /**
     * FAQ 목록을 조회한다.
     */
    fun getFaqs(faqSearchRequestDto: FaqSearchRequestDto): List<FaqListDto> {
        return faqRepository.findFaqs(faqSearchRequestDto)
    }

    /**
     * FAQ 전체 카테고리 조회
     */
    fun findAllFaqGroups(): List<String> {
        return faqRepository.getAllFaqGroupList()
    }

    /**
     * FAQ 카테고리 조회
     */
    fun findFaqGroups(faqGroup: String): List<String> {
        return faqRepository.getFaqGroupList(faqGroup)
    }

    /**
     * FAQ 데이터 상세 조회
     */
    fun getFaq(faqId: String): FaqListDto? {
        val selectedFaq = faqRepository.findByIdOrNull(faqId)
        return if (selectedFaq == null) {
            null
        } else {
            faqMapper.toFaqListDto(selectedFaq)
        }
    }

    /**
     * FAQ 등록
     */
    @Transactional
    fun createFaq(faqDto: FaqDto) {
        val faqEntity = FaqEntity(
            faqGroup = faqDto.faqGroup,
            faqTitle = faqDto.faqTitle,
            faqContent = faqDto.faqContent
        )
        val savedFaq = faqRepository.save(faqEntity)
        aliceFileService.upload(AliceFileDto(ownId = savedFaq.faqId, fileSeq = faqDto.fileSeq))
    }

    /**
     * FAQ 변경
     */
    @Transactional
    fun updateFaq(faqId: String, faqDto: FaqDto) {
        val faqEntity = faqRepository.getOne(faqId)
        faqEntity.faqGroup = faqDto.faqGroup
        faqEntity.faqTitle = faqDto.faqTitle
        faqEntity.faqContent = faqDto.faqContent
        faqRepository.save(faqEntity)
        aliceFileService.upload(AliceFileDto(ownId = faqId, fileSeq = faqDto.fileSeq, delFileSeq = faqDto.delFileSeq))
    }

    /**
     * FAQ 삭제
     */
    @Transactional
    fun deleteFaq(faqId: String) {
        faqRepository.deleteById(faqId)
        aliceFileService.delete(faqId)
    }
}
