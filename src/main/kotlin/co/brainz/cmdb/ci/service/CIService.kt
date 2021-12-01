/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.service

import co.brainz.cmdb.ci.entity.CIDataEntity
import co.brainz.cmdb.ci.entity.CIDataHistoryEntity
import co.brainz.cmdb.ci.entity.CIEntity
import co.brainz.cmdb.ci.entity.CIGroupListDataEntity
import co.brainz.cmdb.ci.entity.CIGroupListDataHistoryEntity
import co.brainz.cmdb.ci.entity.CIHistoryEntity
import co.brainz.cmdb.ci.entity.CIInstanceRelationEntity
import co.brainz.cmdb.ci.repository.CIDataHistoryRepository
import co.brainz.cmdb.ci.repository.CIDataRepository
import co.brainz.cmdb.ci.repository.CIGroupListDataHistoryRepository
import co.brainz.cmdb.ci.repository.CIGroupListDataRepository
import co.brainz.cmdb.ci.repository.CIHistoryRepository
import co.brainz.cmdb.ci.repository.CIInstanceRelationRepository
import co.brainz.cmdb.ci.repository.CIRepository
import co.brainz.cmdb.ciAttribute.repository.CIAttributeRepository
import co.brainz.cmdb.ciClass.service.CIClassService
import co.brainz.cmdb.ciRelation.entity.CIRelationEntity
import co.brainz.cmdb.ciRelation.repository.CIRelationRepository
import co.brainz.cmdb.ciType.entity.CITypeEntity
import co.brainz.cmdb.ciType.repository.CITypeRepository
import co.brainz.cmdb.ciType.service.CITypeService
import co.brainz.cmdb.constants.RestTemplateConstants
import co.brainz.cmdb.dto.CIDetailDto
import co.brainz.cmdb.dto.CIDto
import co.brainz.cmdb.dto.CIHistoryDto
import co.brainz.cmdb.dto.CIListDto
import co.brainz.cmdb.dto.CIListReturnDto
import co.brainz.cmdb.dto.CIRelationDto
import co.brainz.cmdb.dto.CIsDto
import co.brainz.cmdb.dto.RestTemplateReturnDto
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.entity.AliceTagEntity
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.framework.tag.service.AliceTagService
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.cmdb.ci.dto.CISearchCondition
import co.brainz.workflow.instance.repository.WfInstanceRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import javax.transaction.Transactional
import kotlin.math.ceil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CIService(
    private val ciRepository: CIRepository,
    private val ciTypeRepository: CITypeRepository,
    private val ciAttributeRepository: CIAttributeRepository,
    private val ciRelationRepository: CIRelationRepository,
    private val ciDataRepository: CIDataRepository,
    private val ciGroupListDataRepository: CIGroupListDataRepository,
    private val ciHistoryRepository: CIHistoryRepository,
    private val ciDataHistoryRepository: CIDataHistoryRepository,
    private val ciGroupListDataHistoryRepository: CIGroupListDataHistoryRepository,
    private val ciTypeService: CITypeService,
    private val ciClassService: CIClassService,
    private val wfInstanceRepository: WfInstanceRepository,
    private val ciInstanceRelationRepository: CIInstanceRelationRepository,
    private val aliceUserRepository: AliceUserRepository,
    private val aliceTagRepository: AliceTagRepository,
    private val aliceTagService: AliceTagService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * CI 목록 조회.
     */
    fun getCIs(ciSearchCondition: CISearchCondition): CIListReturnDto {
        val cis = ciRepository.findCIList(ciSearchCondition)
        val ciList = mutableListOf<CIListDto>()
        for (ci in cis.results) {
            ciList.add(
                this.makeCIListDto(ci)
            )
        }

        return CIListReturnDto(
            data = ciList,
            paging = AlicePagingData(
                totalCount = cis.total,
                totalCountWithoutCondition = ciRepository.count(),
                currentPageNum = ciSearchCondition.pageNum,
                totalPageNum = ceil(cis.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    /**
     * CI 전체 목록 조회
     */
    fun getCIList(): List<CIListDto> {
        val ciEntities = ciRepository.findAll()
        val ciList = mutableListOf<CIListDto>()

        ciEntities.forEach { ci ->
            val ciListDto = CIListDto(
                ciId = ci.ciId,
                ciNo = ci.ciNo,
                ciName = ci.ciName,
                ciIcon = ci.ciTypeEntity.typeIcon,
                ciIconData = ci.ciTypeEntity.typeIcon?.let { ciTypeService.getCITypeImageData(it) },
                ciDesc = ci.ciDesc,
                ciStatus = ci.ciStatus,
                interlink = ci.interlink,
                typeId = ci.ciTypeEntity.typeId,
                typeName = ci.ciTypeEntity.typeName,
                classId = ci.ciTypeEntity.ciClass.classId,
                className = ci.ciTypeEntity.ciClass.className,
                createUserKey = ci.createUser?.userKey,
                createDt = ci.createDt,
                updateUserKey = ci.updateUser?.userKey,
                updateDt = ci.updateDt,
                ciTags = aliceTagService.getTagsByTargetId(AliceTagConstants.TagType.CI.code, ci.ciId)
            )
            ciList.add(ciListDto)
        }

        return ciList
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
            ciDetailDto.interlink = ciEntity.interlink
            ciDetailDto.typeId = ciEntity.ciTypeEntity.typeId
            ciDetailDto.typeName = ciEntity.ciTypeEntity.typeName
            ciDetailDto.classId = ciEntity.ciTypeEntity.ciClass.classId
            ciDetailDto.className = ciEntity.ciTypeEntity.ciClass.className
            ciDetailDto.createUserKey = ciEntity.createUser?.userKey
            ciDetailDto.createDt = ciEntity.createDt
            ciDetailDto.updateUserKey = ciEntity.updateUser?.userKey
            ciDetailDto.updateDt = ciEntity.updateDt
            ciDetailDto.ciTags = aliceTagService.getTagsByTargetId(AliceTagConstants.TagType.CI.code, ciEntity.ciId)
            ciDetailDto.ciRelations = ciRelationRepository.selectByCiId(ciEntity.ciId)
            ciDetailDto.classes = ciClassService.getCIClassAttributes(
                ciEntity.ciId,
                ciEntity.ciTypeEntity.ciClass.classId
            )
        }
        return ciDetailDto
    }

    /**
     * CI 등록.
     */
    @Transactional
    fun createCI(ciDto: CIDto): RestTemplateReturnDto {
        val restTemplateReturnDto = RestTemplateReturnDto()
        val existCount = 0L
        val ciTypeEntity = ciTypeRepository.getOne(ciDto.typeId)
        val ciNo = this.getCINo(ciTypeEntity)

        when (existCount) {
            0L -> {

                // CIEntity 등록
                var ciEntity = CIEntity(
                    ciId = ciDto.ciId,
                    ciNo = ciNo,
                    ciName = ciDto.ciName,
                    ciStatus = ciDto.ciStatus,
                    ciTypeEntity = ciTypeRepository.getOne(ciDto.typeId),
                    ciDesc = ciDto.ciDesc,
                    interlink = ciDto.interlink,
                    instance = ciDto.instanceId?.let { wfInstanceRepository.findByInstanceId(it) },
                    createDt = LocalDateTime.now(),
                    createUser = ciDto.createUserKey?.let {
                        aliceUserRepository.findAliceUserEntityByUserKey(it)
                    }
                )

                ciEntity = ciRepository.save(ciEntity)

                // CIDataEntity 등록
                ciDto.ciDataList?.forEach { ciData ->
                    val ciAttributeEntity = ciAttributeRepository.getOne(ciData.attributeId)
                    ciDataRepository.save(
                        CIDataEntity(
                            ci = ciEntity,
                            ciAttribute = ciAttributeEntity,
                            value = ciData.attributeData
                        )
                    )
                    if (!ciData.childAttributes.isNullOrEmpty()) {
                        // CIGroupListDataEntity 등록
                        ciData.childAttributes?.forEach { groupListData ->
                            ciGroupListDataRepository.save(
                                CIGroupListDataEntity(
                                    ci = ciEntity,
                                    ciAttribute = ciAttributeEntity,
                                    cAttributeId = groupListData.cAttributeId,
                                    cAttributeSeq = groupListData.cAttributeSeq,
                                    cValue = groupListData.cValue
                                )
                            )
                        }
                    }
                }

                // CITagEntity 등록
                ciDto.ciTags?.forEach {
                    val ciTag = it.asJsonObject
                    aliceTagService.insertTag(
                        AliceTagDto(
                            tagType = AliceTagConstants.TagType.CI.code,
                            tagValue = ciTag.get("tagValue").asString,
                            targetId = ciEntity.ciId
                        )
                    )
                }

                // CIRelationEntity 등록
                ciDto.ciRelations?.forEach {
                    ciRelationRepository.save(
                        CIRelationEntity(
                            relationType = it.relationType,
                            ciId = ciDto.ciId,
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
    @Transactional
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
            this.saveCIHistory(ciEntity, ciDto.interlink)

            ciEntity.ciNo = ciDto.ciNo
            ciDto.ciName.let { ciEntity.ciName = ciDto.ciName }
            ciDto.updateUserKey?.let { ciEntity.updateUser = aliceUserRepository.findAliceUserEntityByUserKey(it) }
            ciDto.ciStatus.let { ciEntity.ciStatus = ciDto.ciStatus }
            ciDto.ciIcon?.let { ciEntity.ciTypeEntity.typeIcon = ciDto.ciIcon }
            ciDto.ciDesc?.let { ciEntity.ciDesc = ciDto.ciDesc }
            ciDto.interlink.let { ciEntity.interlink = ciDto.interlink }
            ciEntity.instance = ciDto.instanceId?.let { wfInstanceRepository.findByInstanceId(it) }
        }
        ciEntity = ciRepository.save(ciEntity)

        // CIDataEntity Update
        ciEntity.ciDataEntities.clear()
        ciDataRepository.flush()

        ciDto.ciDataList?.forEach { ciData ->
            val ciAttributeEntity = ciAttributeRepository.getOne(ciData.attributeId)
            ciDataRepository.save(
                CIDataEntity(
                    ci = ciEntity,
                    ciAttribute = ciAttributeEntity,
                    value = ciData.attributeData
                )
            )
            if (!ciData.childAttributes.isNullOrEmpty()) {
                ciGroupListDataRepository.deleteByCi_CiIdAndCiAttribute_AttributeId(ciDto.ciId, ciData.attributeId)
                ciData.childAttributes?.forEach { groupListData ->
                    ciGroupListDataRepository.save(
                        CIGroupListDataEntity(
                            ci = ciEntity,
                            ciAttribute = ciAttributeEntity,
                            cAttributeId = groupListData.cAttributeId,
                            cAttributeSeq = groupListData.cAttributeSeq,
                            cValue = groupListData.cValue
                        )
                    )
                }
            }
        }

        // CITagEntity Update
        aliceTagRepository.deleteByTargetId(AliceTagConstants.TagType.CI.code, ciDto.ciId)
        ciDto.ciTags?.forEach {
            val ciTag = it.asJsonObject
            aliceTagRepository.save(
                AliceTagEntity(
                    tagType = AliceTagConstants.TagType.CI.code,
                    tagValue = ciTag.get("tagValue").asString,
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
                    ciId = ciDto.ciId,
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
    @Transactional
    fun deleteCI(ciDto: CIDto): RestTemplateReturnDto {
        val restTemplateReturnDto = RestTemplateReturnDto()

        val ciEntity = ciRepository.findByCiId(ciDto.ciId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CI Entity]"
        )

        // 삭제전 마지막 값을 이력에 저장
        ciEntity.updateDt = LocalDateTime.now() // 반영일시
        this.saveCIHistory(ciEntity, ciDto.interlink)

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
    private fun saveCIHistory(ciEntity: CIEntity, interlinkVal: Boolean) {
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
            interlink = interlinkVal,
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

        // CI Group List Data History
        val ciGroupListDataHistoryList = mutableListOf<CIGroupListDataHistoryEntity>()
        if (ciEntity.ciGroupListDataEntities.isNotEmpty()) {
            val childAttributeIdList = mutableListOf<String>()
            for (data in ciEntity.ciGroupListDataEntities) {
                if (!childAttributeIdList.contains(data.cAttributeId)) {
                    childAttributeIdList.add(data.cAttributeId)
                }
            }
            val ciAttributeQueryResult = ciAttributeRepository.findAttributeListInGroupList(childAttributeIdList)
            ciEntity.ciGroupListDataEntities.forEach { data ->
                loop@ for (attribute in ciAttributeQueryResult.results) {
                    if (attribute.attributeId == data.cAttributeId) {
                        ciGroupListDataHistoryList.add(
                            CIGroupListDataHistoryEntity(
                                dataHistoryId = "",
                                seq = historySeq,
                                ciId = ciEntity.ciId,
                                attributeId = data.ciAttribute.attributeId,
                                cAttributeId = data.cAttributeId,
                                cAttributeSeq = data.cAttributeSeq,
                                cAttributeName = attribute.attributeName,
                                cAttributeDesc = attribute.attributeDesc,
                                cAttributeType = attribute.attributeType,
                                cAttributeText = attribute.attributeText,
                                cAttributeValue = attribute.attributeValue,
                                cValue = data.cValue
                            )
                        )
                        break@loop
                    }
                }
            }
        }

        if (ciGroupListDataHistoryList.isNotEmpty()) {
            ciGroupListDataHistoryRepository.saveAll(ciGroupListDataHistoryList)
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
        val tagList = aliceTagRepository.findByTargetId(AliceTagConstants.TagType.CI.code, ci.ciId)

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
            interlink = ci.interlink,
            ciTags = tagList,
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
