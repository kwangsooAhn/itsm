/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.service

import co.brainz.cmdb.ci.entity.CIComponentDataEntity
import co.brainz.cmdb.ci.repository.CIComponentDataRepository
import co.brainz.cmdb.ci.repository.CIRepository
import co.brainz.cmdb.provider.dto.CIComponentDataDto
import co.brainz.cmdb.provider.dto.CIComponentDetail
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CIService(
    private val ciRepository: CIRepository,
    private val ciComponentDataRepository: CIComponentDataRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI 컴포넌트 - CI 세부 정보 등록 / 수정
     */
    fun saveCIComponentData(ciComponentDataDto: CIComponentDataDto): Boolean {
        val ciComponentDetail = CIComponentDetail(
            ciAttributes = ciComponentDataDto.values.ciAttributes,
            ciTags = ciComponentDataDto.values.ciTags
        )
        val ciComponentEntity = CIComponentDataEntity(
            ciId = ciComponentDataDto.ciId,
            componentId = ciComponentDataDto.componentId,
            values = ciComponentDetail.toString(),
            instanceId = ciComponentDataDto.instanceId
        )
        ciComponentDataRepository.save(ciComponentEntity)
        return true
    }

    /**
     * CI 컴포넌트 - CI 세부 정보 삭제
     */
    fun deleteCIComponentData(ciId: String, componentId: String): Boolean {
        val ciComponentEntity = ciComponentDataRepository.findByCiIdAnAndComponentId(ciId, componentId)
        if (ciComponentEntity != null) {
            ciComponentDataRepository.deleteByCiIdAndAndComponentId(ciId, componentId)
            return true
        }
        return false
    }
}
