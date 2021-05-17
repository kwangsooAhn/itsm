/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.service

import co.brainz.cmdb.ci.entity.CIDataEntity
import co.brainz.cmdb.ci.entity.CIDataHistoryEntity
import co.brainz.cmdb.ci.entity.CIEntity
import co.brainz.cmdb.ci.entity.CIHistoryEntity
import co.brainz.cmdb.ci.entity.CIInstanceRelationEntity
import co.brainz.cmdb.ci.repository.CIDataHistoryRepository
import co.brainz.cmdb.ci.repository.CIDataRepository
import co.brainz.cmdb.ci.repository.CIHistoryRepository
import co.brainz.cmdb.ci.repository.CIInstanceRelationRepository
import co.brainz.cmdb.ci.repository.CIRepository
import co.brainz.cmdb.ciAttribute.repository.CIAttributeRepository
import co.brainz.cmdb.ciClass.constants.CIClassConstants
import co.brainz.cmdb.ciClass.entity.CIClassEntity
import co.brainz.cmdb.ciClass.repository.CIClassRepository
import co.brainz.cmdb.ciRelation.entity.CIRelationEntity
import co.brainz.cmdb.ciRelation.repository.CIRelationRepository
import co.brainz.cmdb.ciType.entity.CITypeEntity
import co.brainz.cmdb.ciType.repository.CITypeRepository
import co.brainz.cmdb.ciType.service.CITypeService
import co.brainz.cmdb.constants.RestTemplateConstants
import co.brainz.cmdb.dto.CIClassDetailValueDto
import co.brainz.cmdb.dto.CIDetailDto
import co.brainz.cmdb.dto.CIDto
import co.brainz.cmdb.dto.CIHistoryDto
import co.brainz.cmdb.dto.CIListDto
import co.brainz.cmdb.dto.CIRelationDto
import co.brainz.cmdb.dto.CIReturnDto
import co.brainz.cmdb.dto.CISearchDto
import co.brainz.cmdb.dto.CIsDto
import co.brainz.cmdb.dto.RestTemplateReturnDto
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.entity.AliceTagEntity
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.workflow.instance.repository.WfInstanceRepository
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CIService(
    private val ciRepository: CIRepository,
    private val ciTypeRepository: CITypeRepository,
    private val ciClassRepository: CIClassRepository,
    private val ciAttributeRepository: CIAttributeRepository,
    private val ciRelationRepository: CIRelationRepository,
    private val ciDataRepository: CIDataRepository,
    private val ciHistoryRepository: CIHistoryRepository,
    private val ciDataHistoryRepository: CIDataHistoryRepository,
    private val ciTypeService: CITypeService,
    private val wfInstanceRepository: WfInstanceRepository,
    private val ciInstanceRelationRepository: CIInstanceRelationRepository,
    private val aliceUserRepository: AliceUserRepository,
    private val aliceTagRepository: AliceTagRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI 목록 조회.
     */
    fun getCIs(parameters: LinkedHashMap<String, Any>): CIReturnDto {
        var tagList = emptyList<String>()
        if (parameters["tags"] != null && parameters["tags"].toString() != "") {
            tagList = parameters["tags"].toString()
                .replace("#", "")
                .split(",")
        }
        var search: String? = null
        var offset: Long? = null
        var limit: Long? = null
        var flag: String? = null
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["offset"] != null) offset = parameters["offset"].toString().toLong()
        if (parameters["limit"] != null) limit = parameters["limit"].toString().toLong()
        if (parameters["flag"] != null) flag = parameters["flag"].toString()
        val ciSearchDto = CISearchDto(
            search = search,
            offset = offset,
            limit = limit,
            flag = flag,
            tags = tagList
        )
        val cis = ciRepository.findCIList(ciSearchDto)
        val ciList = mutableListOf<CIListDto>()
        for (ci in cis.results) {
            ciList.add(
                this.makeCIListDto(ci)
            )
        }
        return CIReturnDto(
            data = ciList,
            totalCount = cis.total
        )
    }

    /**
     * CI 목록 단일 조회
     */
    fun getCI(ciId: String): CIListDto {
        val ci = ciRepository.findCI(ciId)
        return this.makeCIListDto(ci)
    }

    /**
     * CI 상세 조회
     */
    fun getCIDetail(ciId: String): CIDetailDto {
        val ciDetailDto = CIDetailDto(
            ciId = ciId
        )
        val ciEntity = ciRepository.findByCiId(ciId)
        if (ciEntity != null) {
            ciDetailDto.ciNo = ciEntity.ciNo
            ciDetailDto.ciName = ciEntity.ciName
            ciDetailDto.ciIcon = ciEntity.ciTypeEntity.typeIcon
            ciDetailDto.ciIconData = ciEntity.ciTypeEntity.typeIcon?.let { ciTypeService.getCITypeImageData(it) }
            ciDetailDto.ciDesc = ciEntity.ciDesc
            ciDetailDto.ciStatus = ciEntity.ciStatus
            ciDetailDto.automatic = ciEntity.automatic
            ciDetailDto.typeId = ciEntity.ciTypeEntity.typeId
            ciDetailDto.typeName = ciEntity.ciTypeEntity.typeName
            ciDetailDto.classId = ciEntity.ciTypeEntity.ciClass.classId
            ciDetailDto.className = ciEntity.ciTypeEntity.ciClass.className
            ciDetailDto.createUserKey = ciEntity.createUser?.userKey
            ciDetailDto.createDt = ciEntity.createDt
            ciDetailDto.updateUserKey = ciEntity.updateUser?.userKey
            ciDetailDto.updateDt = ciEntity.updateDt
            ciDetailDto.ciTags = aliceTagRepository.findByTargetId(AliceTagConstants.TagType.CI.code, ciEntity.ciId)
            ciDetailDto.ciRelations = ciRelationRepository.selectByCiId(ciEntity.ciId)
            ciDetailDto.classes = getAttributeValueAll(ciEntity.ciId, ciEntity.ciTypeEntity.ciClass.classId)
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
                    true -> ciTypeRepository.getOne(ciDto.typeId).ciClass
                    false -> ciClassRepository.getOne(ciDto.classId)
                }

                // CIEntity 등록
                var ciEntity = CIEntity(
                    ciId = ciDto.ciId,
                    ciNo = ciNo,
                    ciName = ciDto.ciName,
                    ciStatus = ciDto.ciStatus,
                    ciTypeEntity = ciTypeRepository.getOne(ciDto.typeId),
                    ciDesc = ciDto.ciDesc,
                    automatic = ciDto.automatic,
                    instance = ciDto.instanceId?.let { wfInstanceRepository.findByInstanceId(it) },
                    createDt = LocalDateTime.now(),
                    createUser = ciDto.createUserKey?.let {
                        aliceUserRepository.findAliceUserEntityByUserKey(it)
                    }
                )

                ciEntity = ciRepository.save(ciEntity)

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
                    aliceTagRepository.save(
                        AliceTagEntity(
                            tagType = AliceTagConstants.TagType.CI.code,
                            tagValue = it.value,
                            targetId = ciEntity.ciId
                        )
                    )
                }

                // CIRelationEntity 등록
                ciDto.ciRelations?.forEach {
                    ciRelationRepository.save(
                        CIRelationEntity(
                            relationType = it.relationType,
                            sourceCIId = it.sourceCIId,
                            targetCIId = it.targetCIId
                        )
                    )
                }

                // 관련 문서 저장
                this.saveCIInstanceRelation(ciEntity)
            }
            else -> {
                restTemplateReturnDto.code = RestTemplateConstants.Status.STATUS_ERROR_DUPLICATION.code
                restTemplateReturnDto.status = false
            }
        }

        return restTemplateReturnDto
    }

    /**
     * CI 업데이트.
     */
    fun updateCI(ciDto: CIDto): RestTemplateReturnDto {
        val restTemplateReturnDto = RestTemplateReturnDto()
        val findCIEntity = ciRepository.findById(ciDto.ciId)
        var ciEntity = findCIEntity.get()

        if (findCIEntity.isEmpty) {
            throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[CI Entity]"
            )
        } else {
            // 변경전 데이터를 이력에 저장
            ciEntity.updateDt = LocalDateTime.now() // 반영일시
            this.saveCIHistory(ciEntity)

            ciEntity.ciNo = ciDto.ciNo
            ciDto.updateUserKey?.let { ciEntity.updateUser = aliceUserRepository.findAliceUserEntityByUserKey(it) }
            ciDto.ciName.let { ciEntity.ciName = ciDto.ciName }
            ciDto.ciStatus.let { ciEntity.ciStatus = ciDto.ciStatus }
            ciDto.ciIcon?.let { ciEntity.ciTypeEntity.typeIcon = ciDto.ciIcon }
            ciDto.ciDesc?.let { ciEntity.ciDesc = ciDto.ciDesc }
            ciDto.automatic?.let { ciEntity.automatic = ciDto.automatic }
            ciEntity.instance = ciDto.instanceId?.let { wfInstanceRepository.findByInstanceId(it) }
        }
        ciEntity = ciRepository.save(ciEntity)

        // CIDataEntity Update
        ciEntity.ciDataEntities.clear()
        ciDataRepository.flush()

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
        aliceTagRepository.deleteByTargetId(AliceTagConstants.TagType.CI.code, ciDto.ciId)
        ciDto.ciTags?.forEach {
            aliceTagRepository.save(
                AliceTagEntity(
                    tagType = AliceTagConstants.TagType.CI.code,
                    tagValue = it.value,
                    targetId = ciEntity.ciId
                )
            )
        }

        // CIRelationEntity Update
        ciRelationRepository.deleteByCiId(ciDto.ciId)
        ciDto.ciRelations?.forEach {
            ciRelationRepository.save(
                CIRelationEntity(
                    relationType = it.relationType,
                    sourceCIId = it.sourceCIId,
                    targetCIId = it.targetCIId
                )
            )
        }

        // 연관 문서 저장
        this.saveCIInstanceRelation(ciEntity)

        return restTemplateReturnDto
    }

    /**
     * CI 삭제
     */
    fun deleteCI(ciDto: CIDto): RestTemplateReturnDto {
        val restTemplateReturnDto = RestTemplateReturnDto()

        val ciEntity = ciRepository.findByCiId(ciDto.ciId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CI Entity]"
        )

        // 삭제전 마지막 값을 이력에 저장
        ciEntity.updateDt = LocalDateTime.now() // 반영일시
        this.saveCIHistory(ciEntity)

        ciDto.updateUserKey?.let { ciEntity.updateUser = aliceUserRepository.findAliceUserEntityByUserKey(it) }
        ciEntity.ciStatus = RestTemplateConstants.CIStatus.STATUS_DELETE.code
        ciEntity.instance = ciDto.instanceId?.let { wfInstanceRepository.findByInstanceId(it) }
        val ci = ciRepository.save(ciEntity)

        // 연관 문서 저장
        this.saveCIInstanceRelation(ci)

        return restTemplateReturnDto
    }

    /**
     * CI 관련 문서 저장
     */
    private fun saveCIInstanceRelation(ci: CIEntity) {
        val instance = ci.instance
        if (instance != null) {
            val ciInstanceRelationEntity = CIInstanceRelationEntity(
                ci = ci,
                instance = ci.instance!!
            )
            ciInstanceRelationRepository.save(ciInstanceRelationEntity)
        }
    }

    /**
     * CI 이력 저장.
     */
    private fun saveCIHistory(ciEntity: CIEntity) {
        var historySeq = 0
        val latelyHistory = ciHistoryRepository.findByLatelyHistory(ciEntity.ciId)
        if (latelyHistory != null) {
            historySeq = latelyHistory.seq?.plus(1) ?: 0
        }

        // CI History
        val ciHistoryEntity = CIHistoryEntity(
            historyId = "",
            seq = historySeq,
            ciId = ciEntity.ciId,
            ciNo = ciEntity.ciNo,
            ciName = ciEntity.ciName,
            ciDesc = ciEntity.ciDesc,
            typeId = ciEntity.ciTypeEntity.typeId,
            ciIcon = ciEntity.ciTypeEntity.typeIcon,
            ciStatus = ciEntity.ciStatus,
            classId = ciEntity.ciTypeEntity.ciClass.classId,
            automatic = ciEntity.automatic,
            instance = ciEntity.instance,
            applyDt = ciEntity.updateDt
        )
        ciHistoryRepository.save(ciHistoryEntity)

        // CI Data History
        val ciDataHistoryList = mutableListOf<CIDataHistoryEntity>()
        ciEntity.ciDataEntities.forEach { data ->
            ciDataHistoryList.add(
                CIDataHistoryEntity(
                    dataHistoryId = "",
                    seq = historySeq,
                    ciId = ciEntity.ciId,
                    attributeId = data.ciAttribute.attributeId,
                    attributeName = data.ciAttribute.attributeName,
                    attributeDesc = data.ciAttribute.attributeDesc,
                    attributeText = data.ciAttribute.attributeText,
                    attributeType = data.ciAttribute.attributeType,
                    attributeValue = data.ciAttribute.attributeValue,
                    value = data.value
                )
            )
        }
        if (ciDataHistoryList.isNotEmpty()) {
            ciDataHistoryRepository.saveAll(ciDataHistoryList)
        }
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

    /**
     * CI 조회 결과 DTO 변경
     */
    private fun makeCIListDto(ci: CIsDto): CIListDto {
        return CIListDto(
            ciId = ci.ciId,
            ciNo = ci.ciNo,
            ciName = ci.ciName,
            ciStatus = ci.ciStatus,
            typeId = ci.typeId,
            typeName = ci.typeName,
            classId = ci.classId,
            className = ci.className,
            ciIcon = ci.ciIcon,
            ciIconData = ci.ciIcon?.let { ciTypeService.getCITypeImageData(it) },
            ciDesc = ci.ciDesc,
            automatic = ci.automatic,
            tags = aliceTagRepository.findByTargetId(AliceTagConstants.TagType.CI.code, ci.ciId),
            createUserKey = ci.createUserKey,
            createDt = ci.createDt,
            updateUserKey = ci.updateUserKey,
            updateDt = ci.updateDt
        )
    }

    /**
     * CI 히스토리 조회
     */
    fun getHistory(ciId: String): List<CIHistoryDto> {
        return ciHistoryRepository.findAllHistory(ciId)
    }

    fun getRelation(ciId: String): List<CIRelationDto> {
        return ciRelationRepository.selectByCiId(ciId)
    }
}
