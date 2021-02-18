/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.service

import co.brainz.cmdb.ci.entity.CIDataEntity
import co.brainz.cmdb.ci.entity.CIEntity
import co.brainz.cmdb.ci.repository.CIDataRepository
import co.brainz.cmdb.ci.repository.CIRepository
import co.brainz.cmdb.ciAttribute.repository.CIAttributeRepository
import co.brainz.cmdb.ciClass.constants.CIClassConstants
import co.brainz.cmdb.ciClass.entity.CIClassEntity
import co.brainz.cmdb.ciClass.repository.CIClassRepository
import co.brainz.cmdb.ciRelation.entity.CIRelationEntity
import co.brainz.cmdb.ciRelation.repository.CIRelationRepository
import co.brainz.cmdb.ciTag.entity.CITagEntity
import co.brainz.cmdb.ciTag.repository.CITagRepository
import co.brainz.cmdb.ciType.entity.CITypeEntity
import co.brainz.cmdb.ciType.repository.CITypeRepository
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CIClassDetailValueDto
import co.brainz.cmdb.provider.dto.CIDetailDto
import co.brainz.cmdb.provider.dto.CIDto
import co.brainz.cmdb.provider.dto.CIListDto
import co.brainz.cmdb.provider.dto.CITagDto
import co.brainz.cmdb.provider.dto.RestTemplateReturnDto
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CIService(
    private val ciRepository: CIRepository,
    private val ciTagRepository: CITagRepository,
    private val ciTypeRepository: CITypeRepository,
    private val ciClassRepository: CIClassRepository,
    private val ciAttributeRepository: CIAttributeRepository,
    private val ciRelationRepository: CIRelationRepository,
    private val ciDataRepository: CIDataRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI 목록 조회.
     */
    fun getCIs(parameters: LinkedHashMap<String, Any>): List<CIListDto> {

        var tagList = emptyList<String>()
        if (parameters["tags"] != null && parameters["tags"].toString() != "") {
            tagList = parameters["tags"].toString()
                .replace("#", "")
                .split(",")
        }
        val flag = parameters["flag"].toString()
        var search = ""
        var offset: Long? = null
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["offset"] != null) {
            offset = parameters["offset"].toString().toLong()
        }
        val cis = ciRepository.findCIList(search, offset, tagList, flag)
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

    /**
     * CI 단일 조회
     */
    fun getCI(ciId: String): CIDetailDto {
        val ciDetailDto = CIDetailDto(
            ciId = ciId
        )
        val resultCiEntity = ciRepository.findById(ciId)
        if (!resultCiEntity.isEmpty) {
            val ciEntity = resultCiEntity.get()
            ciDetailDto.ciNo = ciEntity.ciNo
            ciDetailDto.ciName = ciEntity.ciName
            ciDetailDto.ciIcon = ciEntity.ciIcon
            ciDetailDto.ciDesc = ciEntity.ciDesc
            ciDetailDto.ciStatus = ciEntity.ciStatus
            ciDetailDto.automatic = ciEntity.automatic
            ciDetailDto.typeId = ciEntity.ciTypeEntity.typeId
            ciDetailDto.typeName = ciEntity.ciTypeEntity.typeName
            ciDetailDto.classId = ciEntity.ciClassEntity.classId
            ciDetailDto.className = ciEntity.ciClassEntity.className
            ciDetailDto.createUserKey = ciEntity.createUser?.userKey
            ciDetailDto.createDt = ciEntity.createDt
            ciDetailDto.updateUserKey = ciEntity.updateUser?.userKey
            ciDetailDto.updateDt = ciEntity.updateDt
            ciDetailDto.ciTags = ciTagRepository.findByCIId(ciEntity.ciId)
            ciDetailDto.ciRelations = ciRelationRepository.selectByCiId(ciEntity.ciId)
            ciDetailDto.classes = getAttributeValueAll(ciEntity.ciId, ciEntity.ciClassEntity.classId)
        }
        return ciDetailDto
    }

    /**
     * CI 상세 조회 시 관련 Class 전체에 대한 속성, 값을 조회
     */
    fun getAttributeValueAll(ciId: String, classId: String): MutableList<CIClassDetailValueDto> {
        val attributeValueAll = mutableListOf<CIClassDetailValueDto>()

        /**
         * MEMO
         *   JPA 에서 recursive 쿼리를 지원하지 않는 관계로 Native Query 사용이 종종 발생.
         *   아래 정도에서는 recursive 쿼리를 안써도 성능에 문제가 없을 듯 하지만
         *   정책적인 결정이 필요하다.
         */
        val classList = mutableListOf<String>()
        var targetClass: CIClassEntity? = null
        var targetClassId: String = classId

        while (targetClassId != CIClassConstants.CI_CLASS_ROOT_ID) {
            var resultCiClass = ciClassRepository.findById(targetClassId)
            if (!resultCiClass.isEmpty) {
                targetClass = resultCiClass.get()
                classList.add(targetClass.classId) // 리스트에 더하기
                targetClassId = targetClass.pClass?.classId ?: CIClassConstants.CI_CLASS_ROOT_ID
            }
        }

        classList.forEach { it ->
            val ciClassDetailValueDto = CIClassDetailValueDto(
                attributes = ciAttributeRepository.findAttributeValueList(ciId, it).toMutableList()
            )
            attributeValueAll.add(ciClassDetailValueDto)
        }
        return attributeValueAll
    }

    /**
     * CI 등록.
     */
    fun createCI(ciDto: CIDto): RestTemplateReturnDto {
        val restTemplateReturnDto = RestTemplateReturnDto()
        val existCount = 0L
        val ciTypeEntity = ciTypeRepository.getOne(ciDto.typeId)
        val ciNo = this.getCINo(ciTypeEntity)

        when (existCount) {
            0L -> {

                // 추후 CIEntity 에서 class_id 를 삭제하는 경우
                // 화면이나 워크플로우 엔진에서 먼저 삭제하는 동안 처리될 수 있도록 임시로 타입으로 찾는 로직.
                val ciClassEntity = when (ciDto.classId.isNullOrEmpty()) {
                    true -> ciTypeRepository.getOne(ciDto.typeId).defaultClass
                    false -> ciClassRepository.getOne(ciDto.classId)
                }

                // CIEntity 등록
                val ciEntity = CIEntity(
                    ciId = ciDto.ciId,
                    ciNo = ciNo,
                    ciName = ciDto.ciName,
                    ciStatus = ciDto.ciStatus,
                    ciTypeEntity = ciTypeRepository.getOne(ciDto.typeId),
                    ciClassEntity = ciClassEntity,
                    ciIcon = ciDto.ciIcon,
                    ciDesc = ciDto.ciDesc,
                    automatic = ciDto.automatic
                )

                ciRepository.save(ciEntity)

                // CIDataEntity 등록
                ciDto.ciDataList?.forEach {
                    ciDataRepository.save(
                        CIDataEntity(
                            ci = ciEntity,
                            ciAttribute = ciAttributeRepository.getOne(it.attributeId),
                            value = it.attributeData
                        )
                    )
                }

                // CITagEntity 등록
                ciDto.ciTags?.forEach {
                    ciTagRepository.save(
                        CITagEntity(
                            ci = ciEntity,
                            tagName = it.tagName
                        )
                    )
                }

                // CIRelationEntity 등록
                ciDto.ciRelations?.forEach {
                    ciRelationRepository.save(
                        CIRelationEntity(
                            relationType = it.relationType,
                            masterCIId = it.masterCIId,
                            slaveCIId = it.slaveCIId
                        )
                    )
                }
            }
            else -> {
                restTemplateReturnDto.code = RestTemplateConstants.Status.STATUS_ERROR_DUPLICATION.code
                restTemplateReturnDto.status = false
            }
        }

        return restTemplateReturnDto
    }

    fun updateCI(ciId: String, ciDto: CIDto): RestTemplateReturnDto {
        val restTemplateReturnDto = RestTemplateReturnDto()
        val findCIEntity = ciRepository.findById(ciDto.ciId)
        val ciEntity = findCIEntity.get()

        if (findCIEntity.isEmpty) {
            throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[CI Entity]"
            )
        } else {
            ciEntity.ciNo = ciDto.ciNo
            ciEntity.ciName = ciDto.ciName
            ciEntity.ciStatus = ciDto.ciStatus
            ciEntity.ciIcon = ciDto.ciIcon
            ciEntity.ciDesc = ciDto.ciDesc
            ciEntity.automatic = ciDto.automatic
        }
        ciRepository.save(ciEntity)

        // CIDataEntity Update
        ciDataRepository.deleteByCiId(ciDto.ciId)
        ciDto.ciDataList?.forEach {
            ciDataRepository.save(
                CIDataEntity(
                    ci = ciEntity,
                    ciAttribute = ciAttributeRepository.getOne(it.attributeId),
                    value = it.attributeData
                )
            )
        }

        // CITagEntity Update
        ciTagRepository.deleteByCiId(ciDto.ciId)
        ciDto.ciTags?.forEach {
            ciTagRepository.save(
                CITagEntity(
                    ci = ciEntity,
                    tagName = it.tagName
                )
            )
        }

        // CIRelationEntity Update
        ciRelationRepository.deleteByCiId(ciDto.ciId)
        ciDto.ciRelations?.forEach {
            ciRelationRepository.save(
                CIRelationEntity(
                    relationType = it.relationType,
                    masterCIId = it.masterCIId,
                    slaveCIId = it.slaveCIId
                )
            )
        }

        return restTemplateReturnDto
    }

    /**
     * CI 삭제
     */
    fun deleteCI(ciId: String): RestTemplateReturnDto {
        val restTemplateReturnDto = RestTemplateReturnDto()

        val ciEntity = ciRepository.findByCiId(ciId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CI Entity]"
        )

        ciEntity.ciStatus = RestTemplateConstants.CIStatus.STATUS_DELETE.code
        ciRepository.save(ciEntity)

        return restTemplateReturnDto
    }

    /**
     * CINo 가져오기 (생성).
     */
    private fun getCINo(ciTypeEntity: CITypeEntity): String {
        // 1. alias 로 연결된 최상위 부모까지 조회하여 prefix 생성
        val ciTypes = ciTypeRepository.findByCITypeAll()
        val typeAliasList = mutableListOf<String>()
        typeAliasList.add(ciTypeEntity.typeAlias.toString())
        getPType(ciTypes, ciTypeEntity, typeAliasList)

        var ciNoPrefix = ""
        typeAliasList.reverse()
        typeAliasList.forEach { alias ->
            if (ciNoPrefix.isNotEmpty()) {
                ciNoPrefix += "_"
            }
            ciNoPrefix += alias
        }

        // 2. 순번 생성 (신규 or 이어서 생성)
        val lastCiEntity = ciRepository.getLastCiByCiNo(ciNoPrefix)
        var newSequence = 1
        if (lastCiEntity != null) {
            val length = lastCiEntity.ciNo?.length
            val sequenceStr = length?.let { lastCiEntity.ciNo?.substring(length.minus(6), it) }
            if (sequenceStr != null) {
                newSequence = sequenceStr.toInt() + 1
            }
        }
        return ciNoPrefix + "_" + String.format("%06d", newSequence)
    }

    /**
     * CINo 재귀함수.
     */
    private fun getPType(ciTypes: List<CITypeEntity>?, ciPType: CITypeEntity, typeAliasList: MutableList<String>) {
        if (ciPType.pType != null) {
            ciPType.pType.typeAlias?.let { typeAliasList.add(it) }
            getPType(ciTypes, ciPType.pType, typeAliasList)
        }
    }
}
