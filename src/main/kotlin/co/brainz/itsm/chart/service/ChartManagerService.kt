/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.itsm.chart.dto.ChartRange
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.form.repository.WfFormRepository
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import org.springframework.stereotype.Service

@Service
class ChartManagerService(
    private val aliceTagRepository: AliceTagRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val wfFormRepository: WfFormRepository,
    private val wfInstanceRepository: WfInstanceRepository
) {

    fun getTagsByTargetId(tagType: String, targetId: String): List<AliceTagDto> {
        return aliceTagRepository.findByTargetId(tagType, targetId)
    }

    fun getTagValueList(tagType: String, tagValue: List<String>): List<AliceTagDto> {
        return aliceTagRepository.findByTagValueIn(tagType, tagValue)
    }

    fun getComponentList(tagTargetIds: List<String>): List<WfComponentEntity> {
        return wfComponentRepository.findByComponentIdIn(tagTargetIds)
    }

    fun getFormList(formIds: List<String>): List<WfFormEntity> {
        return wfFormRepository.findByFormIdIn(formIds)
    }

    fun getInstanceListInTags(tags: Set<String>): List<WfInstanceEntity> {
        return wfInstanceRepository.getInstanceListInTags(tags)
    }

    fun getInstanceListInTag(tagValue: String, range: ChartRange): List<WfInstanceEntity> {
        return wfInstanceRepository.getInstanceListInTag(tagValue, range)
    }
}
