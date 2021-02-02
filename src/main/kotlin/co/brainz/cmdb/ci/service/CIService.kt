/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.service

import co.brainz.cmdb.ci.entity.CIDataEntity
import co.brainz.cmdb.ci.entity.CIEntity
import co.brainz.cmdb.ci.entity.CIRelationEntity
import co.brainz.cmdb.ci.entity.CITagEntity
import co.brainz.cmdb.ci.repository.CIDataRepository
import co.brainz.cmdb.ci.repository.CIRelationRepository
import co.brainz.cmdb.ci.repository.CIRepository
import co.brainz.cmdb.ci.repository.CITagRepository
import co.brainz.cmdb.ciAttribute.repository.CIAttributeRepository
import co.brainz.cmdb.ciClass.repository.CIClassRepository
import co.brainz.cmdb.ciType.repository.CITypeRepository
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CIDto
import co.brainz.cmdb.provider.dto.CIListDto
import co.brainz.cmdb.provider.dto.CITagDto
import co.brainz.cmdb.provider.dto.RestTemplateReturnDto
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

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

    /**
     * CI 등록.
     */
    fun createCI(ciDto: CIDto): RestTemplateReturnDto {
        val restTemplateReturnDto = RestTemplateReturnDto()

        // CI 번호에 대한 룰은 아직 미정.
        // 추후 규칙에 따라 넣게 되면 CI_ID가 아니라 CI_NO로 중복체크가 필요할 듯.
        // val existCount = ciRepository.findDuplicateCiNo(ciDto.ciNo)
        val existCount = 0L;

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
                    ciNo = ciDto.ciNo,
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
        var ciEntity = findCIEntity.get()

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
}
