/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.service

import co.brainz.cmdb.ci.repository.CIRepository
import co.brainz.cmdb.ci.repository.CITagRepository
import co.brainz.cmdb.provider.dto.CIListDto
import co.brainz.cmdb.provider.dto.CITagDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class CIService(
    private val ciRepository: CIRepository,
    private val ciTagRepository: CITagRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

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
