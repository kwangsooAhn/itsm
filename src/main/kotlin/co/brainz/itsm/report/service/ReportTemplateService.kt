/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.service

import co.brainz.cmdb.dto.RestTemplateReturnDto
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.report.dto.ReportTemplateDto
import co.brainz.itsm.report.dto.ReportTemplateListReturnDto
import co.brainz.itsm.report.dto.ReportTemplateSearchDto
import co.brainz.itsm.report.entity.ReportTemplateEntity
import co.brainz.itsm.report.repository.ReportTemplateRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReportTemplateService(
    private val aliceUserRepository: AliceUserRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val reportTemplateRepository: ReportTemplateRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 템플릿 목록 조회
     */
    fun getReportTemplateList(reportTemplateSearchDto: ReportTemplateSearchDto): ReportTemplateListReturnDto {
        val templateList = reportTemplateRepository.getReportTemplateList(reportTemplateSearchDto)
        return ReportTemplateListReturnDto(
            data = templateList.results,
            totalCount = templateList.total
        )
    }

    /**
     * 템플릿 조회
     */
    fun getReportTemplateDetail(templateId: String): ReportTemplateDto {
        return reportTemplateRepository.getReportTemplateDetail(templateId)
    }

    /**
     * 템플릿 저장
     */
    fun saveReportTemplate(templateData: String): RestTemplateReturnDto {
        val templateDto = makeReportTemplateDto(templateData)
        val existCount =
            reportTemplateRepository.findDuplicationTemplateName(templateDto.templateName, templateDto.templateId)
        val restTemplateReturnDto = RestTemplateReturnDto()
        when (existCount) {
            0L -> {
                val templateEntity = ReportTemplateEntity(
                    templateId = "",
                    templateName = templateDto.templateName,
                    templateDesc = templateDto.templateDesc,
                    automatic = templateDto.automatic,
                    createDt = LocalDateTime.now(),
                    createUser = aliceUserRepository.findAliceUserEntityByUserKey(currentSessionUser.getUserKey())
                )
                reportTemplateRepository.save(templateEntity)
            }
            else -> {
                restTemplateReturnDto.code = AliceConstants.Status.STATUS_ERROR_DUPLICATION.code
                restTemplateReturnDto.status = false
            }
        }
        return restTemplateReturnDto
    }

    /**
     * 템플릿 수정
     */
    fun updateReportTemplate(templateData: String): RestTemplateReturnDto {
        val templateDto = makeReportTemplateDto(templateData)
        val templateEntity = reportTemplateRepository.findByTemplateId(templateDto.templateId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[Report Template Entity]"
        )
        val existCount =
            reportTemplateRepository.findDuplicationTemplateName(templateDto.templateName, templateDto.templateId)
        val restTemplateReturnDto = RestTemplateReturnDto()
        when (existCount) {
            0L -> {
                templateEntity.templateName = templateDto.templateName
                templateEntity.templateDesc = templateDto.templateDesc
                templateEntity.automatic = templateDto.automatic
                templateEntity.updateDt = LocalDateTime.now()
                templateEntity.updateUser =
                    aliceUserRepository.findAliceUserEntityByUserKey(currentSessionUser.getUserKey())
                reportTemplateRepository.save(templateEntity)
            }
            else -> {
                restTemplateReturnDto.code = AliceConstants.Status.STATUS_ERROR_DUPLICATION.code
                restTemplateReturnDto.status = false
            }
        }
        return restTemplateReturnDto
    }

    /**
     * Template 데이터 파싱
     */
    private fun makeReportTemplateDto(templateData: String): ReportTemplateDto {
        val map = mapper.readValue(templateData, LinkedHashMap::class.java)
        return ReportTemplateDto(
            templateId = map["templateId"] as String,
            templateName = map["templateName"] as String,
            templateDesc = map["templateDesc"] as String,
            automatic = map["automatic"] as Boolean
        )
    }

    /**
     * Template 삭제
     */
    fun deleteReportTemplate(templateId: String): RestTemplateReturnDto {
        val templateEntity = reportTemplateRepository.findByTemplateId(templateId)
        val restTemplateReturnDto = RestTemplateReturnDto()
        when (templateEntity) {
            null -> {
                restTemplateReturnDto.status = false
                restTemplateReturnDto.code = AliceConstants.Status.STATUS_ERROR_NOT_EXIST.code
            }
            else -> {
                reportTemplateRepository.delete(templateEntity)
            }
        }
        return restTemplateReturnDto
    }
}
