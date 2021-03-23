/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.service

import co.brainz.cmdb.ciAttribute.repository.CIAttributeRepository
import co.brainz.cmdb.ciClass.constants.CIClassConstants
import co.brainz.cmdb.ciClass.entity.CIClassAttributeMapEntity
import co.brainz.cmdb.ciClass.entity.CIClassAttributeMapPk
import co.brainz.cmdb.ciClass.entity.CIClassEntity
import co.brainz.cmdb.ciClass.repository.CIClassAttributeMapRepository
import co.brainz.cmdb.ciClass.repository.CIClassRepository
import co.brainz.cmdb.provider.dto.CIClassDetailDto
import co.brainz.cmdb.provider.dto.CIClassDetailValueDto
import co.brainz.cmdb.provider.dto.CIClassDto
import co.brainz.cmdb.provider.dto.CIClassListDto
import co.brainz.cmdb.provider.dto.CIClassReturnDto
import co.brainz.cmdb.provider.dto.CIClassToAttributeDto
import co.brainz.cmdb.provider.dto.CIClassTreeListDto
import co.brainz.cmdb.provider.dto.SearchDto
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import com.querydsl.core.QueryResults
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CIClassService(
    private val ciClassRepository: CIClassRepository,
    private val ciAttributeRepository: CIAttributeRepository,
    private val aliceUserRepository: AliceUserRepository,
    private val ciClassAttributeMapRepository: CIClassAttributeMapRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CMDB CI Class 목록 조회
     */
    fun getCIClasses(parameters: LinkedHashMap<String, Any>): CIClassReturnDto {
        var search: String? = null
        var offset: Long? = null
        var limit: Long? = null
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["offset"] != null) offset = parameters["offset"] as Long?
        if (parameters["limit"] != null) limit = parameters["limit"] as Long?
        val searchDto = SearchDto(
            search = search,
            offset = offset,
            limit = limit
        )
        val ciClasses = ciClassRepository.findClassList(searchDto)
        return CIClassReturnDto(
            data = ciClasses.results,
            totalCount = ciClasses.total
        )
    }

    /**
     * CMDB CI Class 단일 조회
     */
    fun getCIClass(classId: String): CIClassListDto {
        return ciClassRepository.findClass(classId)
    }

    /**
     * CMDB CI Class 상세 조회
     */
    fun getCIClassDetail(classId: String): CIClassDetailDto {
        val ciClassEntity = ciClassRepository.getOne(classId)
        var editable = true
        val classList = mutableListOf<String>()
        classList.add(ciClassEntity.classId)
        val recursiveClassList = mutableListOf<String>()
        val attributes = ciClassRepository.findClassToAttributeList(classList)
        var extendsAttributes: List<CIClassToAttributeDto>? = null

        val pClassName = ciClassEntity.pClass?.className

        if (ciClassRepository.existsByPClass(
                ciClassRepository.findById(classId).orElse(CIClassEntity(classId = classId))
            )
        ) {
            editable = false
        }

        if (ciClassRepository.findRecursiveClass(classId).isNotEmpty()) {
            ciClassRepository.findRecursiveClass(classId).forEach {
                recursiveClassList.add(it.classId)
            }
            extendsAttributes = ciClassRepository.findClassToAttributeList(recursiveClassList)
        }

        return CIClassDetailDto(
            classId = ciClassEntity.classId,
            className = ciClassEntity.className,
            classDesc = ciClassEntity.classDesc,
            pClassId = ciClassEntity.pClass?.classId,
            pClassName = pClassName,
            editable = editable,
            attributes = attributes,
            extendsAttributes = extendsAttributes
        )
    }

    /**
     * CMDB CI Class 트리 조회
     */
    fun getCIClassesTreeNode(parameters: LinkedHashMap<String, Any>): List<CIClassTreeListDto> {
        var search = ""
        if (parameters["search"] != null) search = parameters["search"].toString()

        val treeClassList = mutableListOf<CIClassTreeListDto>()
        val queryResults: QueryResults<CIClassEntity> = ciClassRepository.findClassEntityList(search)
        val returnList: List<CIClassEntity>
        val pClassList = mutableListOf<CIClassEntity>()
        val classIdList = mutableListOf<String>()
        var count = 0
        var classSearchList = queryResults.results

        for (ciClass in classSearchList) {
            var tempClass = ciClass.pClass
            do {
                if (tempClass != null) {
                    pClassList.add(tempClass)
                    tempClass = tempClass.pClass
                }
            } while (tempClass != null)
        }
        if (pClassList.isNotEmpty()) {
            classSearchList.addAll(pClassList)
            classSearchList = classSearchList.distinct()
        }
        for (ciClass in classSearchList) {
            classIdList.add(ciClass.classId)
        }
        val classAttributeMapList = ciClassRepository.findClassToAttributeList(classIdList)

        count = queryResults.total.toInt()
        returnList = classSearchList

        for (ciClassEntity in returnList) {
            var attributeCount = 0
            if (classAttributeMapList != null) {
                for (classAttributeMap in classAttributeMapList) {
                    if (classAttributeMap.classId == ciClassEntity.classId) {
                        attributeCount++
                    }
                }
            }
            treeClassList.add(
                CIClassTreeListDto(
                    classId = ciClassEntity.classId,
                    className = ciClassEntity.className,
                    classDesc = ciClassEntity.classDesc,
                    classLevel = ciClassEntity.classLevel,
                    pClassId = ciClassEntity.pClass?.classId,
                    pClassName = ciClassEntity.pClass?.className,
                    totalCount = count,
                    totalAttributes = attributeCount
                )
            )
        }
        return treeClassList
    }

    /**
     * CMDB CI Class 저장
     */
    fun createCIClass(ciClassDto: CIClassDto): Boolean {
        var order = 1
        val ciClassEntity = CIClassEntity(
            classId = ciClassDto.classId,
            className = ciClassDto.className,
            classDesc = ciClassDto.classDesc,
            pClass = ciClassDto.pClassId?.let {
                ciClassRepository.findById(it).orElse(CIClassEntity(classId = ciClassDto.pClassId!!))
            }
        )
        if (ciClassDto.pClassId.isNullOrEmpty()) {
            ciClassEntity.classLevel = 0
        } else {
            val pCIClassEntity = ciClassRepository.findById(ciClassDto.pClassId!!).get()
            ciClassEntity.classLevel = pCIClassEntity.classLevel?.plus(1)
        }

        val savedCiClassEntity = ciClassRepository.save(ciClassEntity)

        ciClassDto.attributes?.forEach {
            val ciAttributeEntity = ciAttributeRepository.getOne(it)
            ciClassAttributeMapRepository.save(
                CIClassAttributeMapEntity(
                    savedCiClassEntity,
                    ciAttributeEntity,
                    order
                )
            )
            order++
        }

        return true
    }

    /**
     * CMDB CI Class 수정
     */
    fun updateCIClass(ciClassDto: CIClassDto): Boolean {
        var order = 1
        val ciClassEntity = ciClassRepository.findByIdOrNull(ciClassDto.classId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CMDB CLASS Entity]"
        )
        ciClassEntity.className = ciClassDto.className
        ciClassEntity.classDesc = ciClassDto.classDesc
        ciClassEntity.pClass = ciClassDto.pClassId?.let {
            ciClassRepository.findById(it).orElse(CIClassEntity(classId = ciClassDto.pClassId!!))
        }
        ciClassEntity.updateUser = ciClassDto.updateUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        ciClassEntity.updateDt = ciClassDto.updateDt

        val updatedCiClassEntity = ciClassRepository.save(ciClassEntity)

        ciClassEntity.ciClassAttributeMapEntities.forEach {
            ciClassAttributeMapRepository.deleteById(
                CIClassAttributeMapPk(
                    ciClassEntity.classId,
                    it.ciAttribute.attributeId
                )
            )
        }

        ciClassDto.attributes?.forEach {
            val ciAttributeEntity = ciAttributeRepository.getOne(it)
            ciClassAttributeMapRepository.save(
                CIClassAttributeMapEntity(
                    updatedCiClassEntity,
                    ciAttributeEntity,
                    order
                )
            )
            order++
        }

        return true
    }

    /**
     * CMDB CI Class 삭제
     */
    fun deleteCIClass(classId: String): Boolean {
        val classEntity = ciClassRepository.findByIdOrNull(classId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CMDB CLASS Entity]"
        )

        ciClassRepository.deleteById(classEntity.classId)
        return true
    }

    /**
     * Class에 따른 CI 세부 속성 조회
     */
    fun getCIClassAttributes(classId: String): List<CIClassDetailValueDto> {
        val attributeValueAll = mutableListOf<CIClassDetailValueDto>()
        val classList = mutableListOf<String>()
        var targetClass: CIClassEntity? = null
        var targetClassId: String = classId

        while (targetClassId != CIClassConstants.CI_CLASS_ROOT_ID) {
            val resultCiClass = ciClassRepository.findById(targetClassId)
            if (!resultCiClass.isEmpty) {
                targetClass = resultCiClass.get()
                classList.add(targetClass.classId) // 리스트에 더하기
                targetClassId = targetClass.pClass?.classId ?: CIClassConstants.CI_CLASS_ROOT_ID
            }
        }
        classList.reversed().forEach {
            val ciClassDetailValueDto = CIClassDetailValueDto(
                attributes = ciAttributeRepository.findAttributeValueList("", it).toMutableList()
            )
            attributeValueAll.add(ciClassDetailValueDto)
        }
        return attributeValueAll.toList()
    }
}
