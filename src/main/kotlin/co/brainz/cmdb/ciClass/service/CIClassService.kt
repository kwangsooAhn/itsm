/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.service

import co.brainz.cmdb.ciClass.entity.CmdbClassEntity
import co.brainz.cmdb.ciClass.repository.CIClassRepository
import co.brainz.cmdb.provider.dto.CmdbClassDto
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import org.slf4j.LoggerFactory
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
    fun getCmdbClass(classId: String): CmdbClassDto {
        var cmdbClassEntity = ciClassRepository.findByClassId(classId)

        val cmdbClassDto = CmdbClassDto(
            classId = cmdbClassEntity!!.classId,
            className = cmdbClassEntity!!.className,
            classDesc = cmdbClassEntity!!.classDesc,
            pclassId = cmdbClassEntity!!.pClassId
        )
        return cmdbClassDto
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

        ciClassRepository.save(cmdbClassEntity)
        return true
    }

    /**
     * CMDB Class 수정
     */
    fun updateCmdbClass(classId: String, cmdbClassDto: CmdbClassDto): Boolean {
        val cmdbClassEntity = ciClassRepository.findByClassId(classId) ?: throw AliceException(
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

        ciClassRepository.save(cmdbClassEntity)
        return true
    }

    /**
     * CMDB Class 삭제
     */
    fun deleteCmdbClass(classId: String): Boolean {
        val classEntity = ciClassRepository.findByClassId(classId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CMDB CLASS Entity]"
        )

        ciClassRepository.deleteById(classEntity.classId)
        return true
    }
}