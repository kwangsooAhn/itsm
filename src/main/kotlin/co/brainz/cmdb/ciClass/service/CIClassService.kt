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
        val recursiveClassList = mutableListOf<String>()
        val attributes = ciClassRepository.findClassToAttributeList(classList)
        var extendsAtrributes: List<CmdbClassToAttributeDto>? = null

        val pClassName = cmdbClassEntity.pClassId?.let {
            ciClassRepository.getOne(it).className
        }
        classList.add(cmdbClassEntity.classId)

        if (ciClassRepository.findRecursiveClass(classId).isNotEmpty()) {
            ciClassRepository.findRecursiveClass(classId).forEach {
                recursiveClassList.add(it.classId)
            }
            editable = false
            extendsAtrributes = ciClassRepository.findClassToAttributeList(recursiveClassList)
        }

        return CmdbClassDetailDto(
            classId = cmdbClassEntity.classId,
            className = cmdbClassEntity.className,
            classDesc = cmdbClassEntity.classDesc,
            pClassId = cmdbClassEntity.pClassId,
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
        var offset: Long? = null
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["offset"] != null) {
            offset = parameters["offset"].toString().toLong()
        }
        return ciClassRepository.findClassList(search, offset).toList()
    }

    /**
     * CMDB Class 저장
     */
    fun createCmdbClass(cmdbClassDto: CmdbClassDto): Boolean {
        val cmdbClassEntity = CmdbClassEntity(
            classId = cmdbClassDto.classId,
            className = cmdbClassDto.className,
            classDesc = cmdbClassDto.classDesc,
            pClassId = cmdbClassDto.pClassId
        )
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
    fun updateCmdbClass(classId: String, cmdbClassDto: CmdbClassDto): Boolean {
        val cmdbClassEntity = ciClassRepository.findByIdOrNull(classId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CMDB CLASS Entity]"
        )
        cmdbClassEntity.className = cmdbClassDto.className
        cmdbClassEntity.classDesc = cmdbClassDto.classDesc
        cmdbClassEntity.pClassId = cmdbClassDto.pClassId
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
