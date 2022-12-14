/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.service

import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.itsm.statistic.customChart.dto.ChartRange
import co.brainz.itsm.statistic.customChart.dto.ChartTagInstanceDto
import co.brainz.itsm.statistic.customChart.dto.average.ChartTokenData
import co.brainz.itsm.statistic.customChart.dummy.DummyDataComponent
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.form.repository.WfFormRepository
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service

@Service
class ChartManagerService(
    private val aliceTagRepository: AliceTagRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val wfFormRepository: WfFormRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val dummyDataComponent: DummyDataComponent
) {
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getTagValueList(tagType: String, tagValue: List<String>): List<AliceTagDto> {
        return aliceTagRepository.findByTagValueIn(tagType, tagValue)
    }

    fun getComponentList(tagTargetIds: List<String>): List<WfComponentEntity> {
        return wfComponentRepository.findByComponentIdIn(tagTargetIds)
    }

    fun getFormList(formIds: List<String>): List<WfFormEntity> {
        return wfFormRepository.findByFormIdIn(formIds)
    }

    fun getInstanceListInTag(tagValue: String, range: ChartRange, documentStatus: String?): List<WfInstanceEntity> {
        return wfInstanceRepository.getInstanceListInTag(tagValue, range, documentStatus)
    }

    fun getLastTokenList(instanceIds: Set<String>): List<WfTokenEntity> {
        return wfTokenRepository.getEndTokenList(instanceIds)
    }

    fun getTokenDataList(componentIds: Set<String>, tokenIds: Set<String>, componentTypeSet: Set<String>): List<ChartTokenData> {
        return wfTokenDataRepository.getTokenDataList(componentIds, tokenIds, componentTypeSet)
    }

    fun getDummyDataList(aliceTagDto: List<AliceTagDto>): List<ChartTagInstanceDto> {
        return dummyDataComponent.getDummyDataList(aliceTagDto)
    }
}
