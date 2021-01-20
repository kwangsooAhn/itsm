/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciType.service

import co.brainz.cmdb.ciClass.repository.CIClassRepository
import co.brainz.cmdb.ciType.entity.CmdbTypeEntity
import co.brainz.cmdb.ciType.repository.CITypeRepository
import co.brainz.cmdb.provider.dto.CmdbTypeDto
import co.brainz.cmdb.provider.dto.CmdbTypeListDto
import co.brainz.framework.auth.repository.AliceUserRepository
import com.querydsl.core.QueryResults
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CITypeService(
    private val ciTypeRepository: CITypeRepository,
    private val ciClassRepository: CIClassRepository,
    private val aliceUserRepository: AliceUserRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     *  CMDB Type 트리 조회
     */
    fun getCmdbTypes(searchValue: String): List<CmdbTypeListDto> {
        val treeTypeList = mutableListOf<CmdbTypeListDto>()
        val queryResults: QueryResults<CmdbTypeEntity> = ciTypeRepository.findByTypeList(searchValue)
        val returnList: List<CmdbTypeEntity>
        var count = 0L
        var typeSearchList = queryResults.results

        val pTypeList = mutableListOf<CmdbTypeEntity>()
        for (type in typeSearchList) {
            var tempType = type.pType
            do {
                if (tempType != null) {
                    pTypeList.add(tempType)
                    tempType = tempType.pType
                }
            } while (tempType != null)
        }
        if (pTypeList.isNotEmpty()) {
            typeSearchList.addAll(pTypeList)
            typeSearchList = typeSearchList.distinct()
        }
        count = queryResults.total
        returnList = typeSearchList

        for (typeEntity in returnList) {
            treeTypeList.add(
                CmdbTypeListDto(
                    typeId = typeEntity.typeId,
                    typeName = typeEntity.typeName,
                    typeDesc = typeEntity.typeDesc,
                    typeLevel = typeEntity.typeLevel,
                    ptypeId = typeEntity.pType?.typeId,
                    ptypeName = typeEntity.pType?.typeName,
                    typeIcon = typeEntity.typeIcon,
                    defaultClassId = typeEntity.defaultClass?.classId,
                    defaultClassName = typeEntity.defaultClass?.className,
                    totalCount = count
                )
            )
        }
        return treeTypeList
    }

    /**
     *  CMDB Type 단일 조회
     */
    fun getCmdbType(typeId: String): CmdbTypeDto {
        val typeDetailEntity = ciTypeRepository.findById(typeId).get()
        return CmdbTypeDto(
            typeId = typeDetailEntity.typeId,
            typeName = typeDetailEntity.typeName,
            typeDesc = typeDetailEntity.typeDesc,
            typeLevel = typeDetailEntity.typeLevel,
            ptypeId = typeDetailEntity.pType?.let { typeDetailEntity.pType.typeId!! },
            ptypeName = typeDetailEntity.pType?.let { typeDetailEntity.pType.typeName!! },
            typeIcon = typeDetailEntity.typeIcon,
            defaultClassId = typeDetailEntity.defaultClass?.classId,
            defaultClassName = typeDetailEntity.defaultClass?.className
        )
    }

    /**
     *  CMDB Type 등록
     */
    fun createCmdbType(cmdbTypeDto: CmdbTypeDto): Boolean {
        val cmdbTypeEntity = CmdbTypeEntity(
            pType = ciTypeRepository.findById(cmdbTypeDto.ptypeId!!)
                .orElse(CmdbTypeEntity(typeId = cmdbTypeDto.ptypeId)),
            typeName = cmdbTypeDto.typeName,
            typeDesc = cmdbTypeDto.typeDesc,
            typeIcon = cmdbTypeDto.typeIcon,
            defaultClass = cmdbTypeDto.defaultClassId?.let { ciClassRepository.getOne(it) }
        )
        if (cmdbTypeDto.ptypeId.isNullOrEmpty()) {
            cmdbTypeEntity.typeLevel = 0
        } else {
            val pTypeEntity = ciTypeRepository.findById(cmdbTypeDto.ptypeId!!).get()
            cmdbTypeEntity.typeLevel = pTypeEntity.typeLevel!! + 1
        }
        cmdbTypeEntity.createUser = cmdbTypeDto.createUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        cmdbTypeEntity.createDt = cmdbTypeDto.createDt

        ciTypeRepository.save(cmdbTypeEntity)
        return true
    }

    /**
     *  CMDB Type 수정
     */
    fun updateCmdbType(cmdbTypeDto: CmdbTypeDto, typeId: String): Boolean {
        val cmdbTypeEntity = CmdbTypeEntity(
            typeId = cmdbTypeDto.typeId,
            pType = ciTypeRepository.findById(cmdbTypeDto.ptypeId!!)
                .orElse(CmdbTypeEntity(typeId = cmdbTypeDto.ptypeId)),
            typeName = cmdbTypeDto.typeName,
            typeDesc = cmdbTypeDto.typeDesc,
            typeLevel = cmdbTypeDto.typeLevel,
            typeIcon = cmdbTypeDto.typeIcon,
            defaultClass = cmdbTypeDto.defaultClassId?.let { ciClassRepository.getOne(it) }
        )
        cmdbTypeEntity.updateUser = cmdbTypeDto.updateUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        cmdbTypeEntity.updateDt = cmdbTypeDto.updateDt
        ciTypeRepository.save(cmdbTypeEntity)
        return true
    }

    /**
     *  CMDB Type 삭제
     */
    fun deleteCmdbType(typeId: String): Boolean {
        ciTypeRepository.deleteById(typeId)
        return true
    }
}
