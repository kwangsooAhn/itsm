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
import co.brainz.cmdb.provider.dto.CIClassDetailValueDto
import co.brainz.cmdb.provider.dto.CIClassDetailDto
import co.brainz.cmdb.provider.dto.CIClassDto
import co.brainz.cmdb.provider.dto.CIClassListDto
import co.brainz.cmdb.provider.dto.CIClassToAttributeDto
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
     * CMDB CI Class 단일 조회
     */
    fun getCIClass(classId: String): CIClassDetailDto {
        val ciClassEntity = ciClassRepository.getOne(classId)
        var editable = true
        val classList = mutableListOf<String>()
        classList.add(ciClassEntity.classId)
        val recursiveClassList = mutableListOf<String>()
        val attributes = ciClassRepository.findClassToAttributeList(classList)
        var extendsAtrributes: List<CIClassToAttributeDto>? = null

        val pClassName = ciClassEntity.pClass?.let {
            it.className
        }

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
            extendsAtrributes = ciClassRepository.findClassToAttributeList(recursiveClassList)
        }

        return CIClassDetailDto(
            classId = ciClassEntity.classId,
            className = ciClassEntity.className,
            classDesc = ciClassEntity.classDesc,
            pClassId = ciClassEntity.pClass?.classId,
            pClassName = pClassName,
            editable = editable,
            attributes = attributes,
            extendsAttributes = extendsAtrributes
        )
    }

    /**
     * CMDB CI Class 멀티 조회
     */
    fun getCIClasses(parameters: LinkedHashMap<String, Any>): List<CIClassListDto> {
        var search = ""
        if (parameters["search"] != null) search = parameters["search"].toString()

        val treeClassList = mutableListOf<CIClassListDto>()
        val queryResults: QueryResults<CIClassEntity> = ciClassRepository.findClassList(search)
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
                CIClassListDto(
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
    fun createCIClass(CIClassDto: CIClassDto): Boolean {
        var order = 1
        val ciClassEntity = CIClassEntity(
            classId = CIClassDto.classId,
            className = CIClassDto.className,
            classDesc = CIClassDto.classDesc,
            pClass = CIClassDto.pClassId?.let {
                ciClassRepository.findById(it).orElse(CIClassEntity(classId = CIClassDto.pClassId!!))
            }
        )
        if (CIClassDto.pClassId.isNullOrEmpty()) {
            ciClassEntity.classLevel = 0
        } else {
            val pCIClassEntity = ciClassRepository.findById(CIClassDto.pClassId!!).get()
            ciClassEntity.classLevel = pCIClassEntity.classLevel?.plus(1)
        }

        val savedCiClassEntity = ciClassRepository.save(ciClassEntity)

        CIClassDto.attributes?.forEach {
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
    fun updateCIClass(CIClassDto: CIClassDto): Boolean {
        var order = 1
        val ciClassEntity = ciClassRepository.findByIdOrNull(CIClassDto.classId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CMDB CLASS Entity]"
        )
        ciClassEntity.className = CIClassDto.className
        ciClassEntity.classDesc = CIClassDto.classDesc
        ciClassEntity.pClass = CIClassDto.pClassId?.let {
            ciClassRepository.findById(it).orElse(CIClassEntity(classId = CIClassDto.pClassId!!))
        }
        ciClassEntity.updateUser = CIClassDto.updateUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        ciClassEntity.updateDt = CIClassDto.updateDt

        val updatedCiClassEntity = ciClassRepository.save(ciClassEntity)

        ciClassEntity.ciClassAttributeMapEntities.forEach {
            ciClassAttributeMapRepository.deleteById(
                CIClassAttributeMapPk(
                    ciClassEntity.classId,
                    it.ciAttribute.attributeId
                )
            )
        }

        CIClassDto.attributes?.forEach {
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
    fun getCIClassAttributes(classId: String): MutableList<CIClassDetailValueDto> {
        val attributeValueAll = mutableListOf<CIClassDetailValueDto>()
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

        classList.forEach { classId ->
            val ciClassDetailValueDto = CIClassDetailValueDto(
                attributes = ciAttributeRepository.findAttributeValueList("", classId).toMutableList()
            )
            attributeValueAll.add(ciClassDetailValueDto)
        }
        return attributeValueAll
    }
}
