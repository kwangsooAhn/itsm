/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.component.service

import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.workflow.component.dto.WfComponentTemplateDto
import co.brainz.workflow.component.entity.WfComponentTemplateEntity
import co.brainz.workflow.component.repository.WfComponentTemplateRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WfComponentTemplateService(
    private val wfComponentTemplateRepository: WfComponentTemplateRepository
) {

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 컴포넌트 템플릿 데이터 조회
     * @return ZResponse
     */
    fun getComponentTemplateData(): ZResponse {
        val wfComponentTemplateList: MutableList<WfComponentTemplateDto> = mutableListOf()

        wfComponentTemplateRepository.findAll().let {
            it.forEach { wfComponentTemplateEntity ->
                wfComponentTemplateList.add(
                    WfComponentTemplateDto(
                        templateId = wfComponentTemplateEntity.templateId,
                        templateName = wfComponentTemplateEntity.templateName,
                        type = wfComponentTemplateEntity.componentType,
                        data = mapper.readValue(wfComponentTemplateEntity.componentData, LinkedHashMap::class.java)
                    )
                )
            }
        }

        return ZResponse(
            data = wfComponentTemplateList
        )
    }

    /**
     * 컴포넌트 템플릿 저장
     * @param wfComponentTemplateDto
     * @return ZResponse
     */

    @Transactional
    fun saveComponentTemplate(wfComponentTemplateDto: WfComponentTemplateDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        val wfComponentTemplateEntity = WfComponentTemplateEntity(
            templateId = wfComponentTemplateDto.templateId,
            templateName = wfComponentTemplateDto.templateName,
            componentType = wfComponentTemplateDto.type,
            componentData = mapper.convertValue(wfComponentTemplateDto.data, String::class.java)
        )

        when (wfComponentTemplateRepository.existsByTemplateName(wfComponentTemplateDto.templateName)) {
            true -> {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
            false -> {
                wfComponentTemplateRepository.save(wfComponentTemplateEntity)
            }
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * 컴포넌트 템플릿 삭제
     * @param templateId
     * @return ZResponse
     */

    @Transactional
    fun deleteComponentTemplate(templateId: String): ZResponse {
        wfComponentTemplateRepository.deleteById(templateId)
        return ZResponse()
    }
}
