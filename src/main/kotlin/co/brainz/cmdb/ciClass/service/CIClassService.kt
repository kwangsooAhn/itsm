/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.service

import co.brainz.cmdb.ciClass.entity.CmdbClassEntity
import co.brainz.cmdb.ciClass.repository.CIClassRepository
import co.brainz.cmdb.provider.dto.CmdbClassDetailDto
import co.brainz.cmdb.provider.dto.CmdbClassDto
import co.brainz.cmdb.provider.dto.CmdbClassListDto
import co.brainz.cmdb.provider.dto.CmdbClassToAttributeDto
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
    private val aliceUserRepository: AliceUserRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CMDB Class 단일 조회
     */
    fun getCmdbClass(classId: String): CmdbClassDetailDto {
        val cmdbClassEntity = ciClassRepository.getOne(classId)
        var editable = true
        val classList = mutableListOf<String>()
        classList.add(cmdbClassEntity.classId)
        val recursiveClassList = mutableListOf<String>()
        val attributes = ciClassRepository.findClassToAttributeList(classList)
        var extendsAtrributes: List<CmdbClassToAttributeDto>? = null

        val pClassName = cmdbClassEntity.pClass?.let {
            it.className
        }

        if (ciClassRepository.existsByPClass(
                ciClassRepository.findById(classId).orElse(CmdbClassEntity(classId = classId))
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

        return CmdbClassDetailDto(
            classId = cmdbClassEntity.classId,
            className = cmdbClassEntity.className,
            classDesc = cmdbClassEntity.classDesc,
            pClassId = cmdbClassEntity.pClass?.classId,
            pClassName = pClassName,
            editable = editable,
            attributes = attributes,
            extendsAttributes = extendsAtrributes
        )
    }

    /**
     * CMDB Class 멀티 조회
     */
    fun getCmdbClasses(parameters: LinkedHashMap<String, Any>): List<CmdbClassListDto> {
        var search = ""
        if (parameters["search"] != null) search = parameters["search"].toString()

        val treeClassList = mutableListOf<CmdbClassListDto>()
        val queryResults: QueryResults<CmdbClassEntity> = ciClassRepository.findClassList(search)
        var returnList: List<CmdbClassEntity>
        val pClassList = mutableListOf<CmdbClassEntity>()
        val classIdList = mutableListOf<String>()
        var count = 0
        var classSearchList = queryResults.results

        for (cmdbClass in classSearchList) {
            var tempClass = cmdbClass.pClass
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
        for (cmdbClass in classSearchList) {
            classIdList.add(cmdbClass.classId)
        }
        var classAttributeMapList = ciClassRepository.findClassToAttributeList(classIdList)

        count = queryResults.total.toInt()
        returnList = classSearchList

        for (cmdbClassEntity in returnList) {
            var attributeCount = 0
            if (classAttributeMapList != null) {
                for (classAttributeMap in classAttributeMapList) {
                    if (classAttributeMap.classId == cmdbClassEntity.classId) {
                        attributeCount++
                    }
                }
            }
            treeClassList.add(
                CmdbClassListDto(
                    classId = cmdbClassEntity.classId,
                    className = cmdbClassEntity.className,
                    classDesc = cmdbClassEntity.classDesc,
                    classLevel = cmdbClassEntity.classLevel,
                    pClassId = cmdbClassEntity.pClass?.classId,
                    pClassName = cmdbClassEntity.pClass?.className,
                    totalCount = count,
                    totalAttributes = attributeCount
                )
            )
        }
        return treeClassList
    }

    /**
     * CMDB Class 저장
     */
    fun createCmdbClass(cmdbClassDto: CmdbClassDto): Boolean {
        val cmdbClassEntity = CmdbClassEntity(
            classId = cmdbClassDto.classId,
            className = cmdbClassDto.className,
            classDesc = cmdbClassDto.classDesc,
            pClass = cmdbClassDto.pClassId?.let {
                ciClassRepository.findById(it).orElse(CmdbClassEntity(classId = cmdbClassDto.pClassId!!))
            }
        )
        if (cmdbClassDto.pClassId.isNullOrEmpty()) {
            cmdbClassEntity.classLevel = 0
        } else {
            val pCmdbClassEntity = ciClassRepository.findById(cmdbClassDto.pClassId!!).get()
            cmdbClassEntity.classLevel = pCmdbClassEntity.classLevel?.plus(1)
        }

        cmdbClassEntity.createUser = cmdbClassDto.createUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        cmdbClassEntity.createDt = cmdbClassDto.createDt

        ciClassRepository.save(cmdbClassEntity)
        return true
    }

    /**
     * CMDB Class 수정
     */
    fun updateCmdbClass(cmdbClassDto: CmdbClassDto): Boolean {
        val cmdbClassEntity = ciClassRepository.findByIdOrNull(cmdbClassDto.classId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CMDB CLASS Entity]"
        )
        cmdbClassEntity.className = cmdbClassDto.className
        cmdbClassEntity.classDesc = cmdbClassDto.classDesc
        cmdbClassEntity.pClass = cmdbClassDto.pClassId?.let {
            ciClassRepository.findById(it).orElse(CmdbClassEntity(classId = cmdbClassDto.pClassId!!))
        }
        cmdbClassEntity.updateUser = cmdbClassDto.updateUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        cmdbClassEntity.updateDt = cmdbClassDto.updateDt

        ciClassRepository.save(cmdbClassEntity)
        return true
    }

    /**
     * CMDB Class 삭제
     */
    fun deleteCmdbClass(classId: String): Boolean {
        val classEntity = ciClassRepository.findByIdOrNull(classId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CMDB CLASS Entity]"
        )

        ciClassRepository.deleteById(classEntity.classId)
        return true
    }
}
