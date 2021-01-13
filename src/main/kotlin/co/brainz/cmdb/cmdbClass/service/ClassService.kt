/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.cmdbClass.service

import co.brainz.cmdb.cmdbClass.entity.CmdbClassEntity
import co.brainz.cmdb.cmdbClass.repository.ClassRepository
import co.brainz.cmdb.provider.dto.CmdbClassDto
import co.brainz.cmdb.provider.dto.CmdbClassListDto
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ClassService(
    private val classRepository: ClassRepository,
    private val aliceUserRepository: AliceUserRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CMDB Class 단일 조회
     */
    fun getCmdbClass(classId: String): CmdbClassDto {
        var cmdbClassEntity = classRepository.getOne(classId)

        val cmdbClassDto = CmdbClassDto(
            classId = cmdbClassEntity.classId,
            className = cmdbClassEntity.className,
            classDesc = cmdbClassEntity.classDesc,
            pclassId = cmdbClassEntity.pClassId
        )
        return cmdbClassDto
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
        return classRepository.findClassList(search, offset).toList()
    }

    /**
     * CMDB Class 저장
     */
    fun createCmdbClass(cmdbClassDto: CmdbClassDto): Boolean {
        val cmdbClassEntity = CmdbClassEntity(
            classId = cmdbClassDto.classId,
            className = cmdbClassDto.className,
            classDesc = cmdbClassDto.classDesc,
            pClassId = cmdbClassDto.pclassId
        )
        cmdbClassEntity.createUser = cmdbClassDto.createUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        cmdbClassEntity.createDt = cmdbClassDto.createDt

        classRepository.save(cmdbClassEntity)
        return true
    }

    /**
     * CMDB Class 수정
     */
    fun updateCmdbClass(classId: String, cmdbClassDto: CmdbClassDto): Boolean {
        val cmdbClassEntity = classRepository.findByIdOrNull(classId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CMDB CLASS Entity]"
        )
        cmdbClassEntity.className = cmdbClassDto.className
        cmdbClassEntity.classDesc = cmdbClassDto.classDesc
        cmdbClassEntity.pClassId = cmdbClassDto.pclassId
        cmdbClassEntity.updateUser = cmdbClassDto.updateUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        cmdbClassEntity.updateDt = cmdbClassDto.updateDt

        classRepository.save(cmdbClassEntity)
        return true
    }

    /**
     * CMDB Class 삭제
     */
    fun deleteCmdbClass(classId: String): Boolean {
        val classEntity = classRepository.findByIdOrNull(classId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CMDB CLASS Entity]"
        )

        classRepository.deleteById(classEntity.classId)
        return true
    }
}
