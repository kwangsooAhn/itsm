/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.service

import co.brainz.cmdb.ci.entity.CIComponentDataEntity
import co.brainz.cmdb.ci.repository.CIComponentDataRepository
import co.brainz.cmdb.ci.repository.CIRepository
import co.brainz.cmdb.ci.repository.CITagRepository
import co.brainz.cmdb.provider.dto.CIComponentDataDto
import co.brainz.cmdb.provider.dto.CIComponentDetail
import co.brainz.cmdb.provider.dto.CIListDto
import co.brainz.cmdb.provider.dto.CITagDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class CIService(
    private val ciRepository: CIRepository,
    private val ciTagRepository: CITagRepository,
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

    /**
     * CI 목록 조회.
     */
    fun getCIs(parameters: LinkedHashMap<String, Any>): List<CIListDto> {

        var tagList = emptyList<String>()
        if (parameters["tags"].toString().isNotEmpty()) {
            tagList = StringUtils.trimAllWhitespace(parameters["tags"].toString())
                .replace("#", "")
                .split(",")
        }

        var search = ""
        var offset: Long? = null
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["offset"] != null) {
            offset = parameters["offset"].toString().toLong()
        }
        val cis = ciRepository.findCIList(search, offset, tagList)
        var tags = mutableListOf<CITagDto>()
        val ciList = mutableListOf<CIListDto>()
        for (ci in cis) {
            tags = ciTagRepository.findByCIId(ci.ciId!!)
            ciList.add(
                CIListDto(
                    ciId = ci.ciId,
                    ciNo = ci.ciNo,
                    ciName = ci.ciName,
                    ciStatus = ci.ciStatus,
                    typeId = ci.typeId,
                    typeName = ci.typeName,
                    classId = ci.classId,
                    className = ci.className,
                    ciIcon = ci.ciIcon,
                    ciDesc = ci.ciDesc,
                    automatic = ci.automatic,
                    tags = tags,
                    createUserKey = ci.createUserKey,
                    createDt = ci.createDt,
                    updateUserKey = ci.updateUserKey,
                    updateDt = ci.updateDt,
                    totalCount = ci.totalCount
                )
            )
        }
        return ciList
    }
}
