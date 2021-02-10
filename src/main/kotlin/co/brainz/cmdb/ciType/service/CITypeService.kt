/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciType.service

import co.brainz.cmdb.ciClass.repository.CIClassRepository
import co.brainz.cmdb.ciType.constants.CITypeConstants
import co.brainz.cmdb.ciType.entity.CITypeEntity
import co.brainz.cmdb.ciType.repository.CITypeRepository
import co.brainz.cmdb.provider.dto.CITypeDto
import co.brainz.cmdb.provider.dto.CITypeListDto
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
    fun getCITypes(searchValue: String): List<CITypeListDto> {
        val treeTypeList = mutableListOf<CITypeListDto>()
        val queryResults: QueryResults<CITypeEntity> = ciTypeRepository.findByTypeList(searchValue)
        val returnList: List<CITypeEntity>
        var count = 0L
        var typeSearchList = queryResults.results

        val pTypeList = mutableListOf<CITypeEntity>()
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
                CITypeListDto(
                    typeId = typeEntity.typeId,
                    typeName = typeEntity.typeName,
                    typeDesc = typeEntity.typeDesc,
                    typeLevel = typeEntity.typeLevel,
                    typeAlias = typeEntity.typeAlias,
                    pTypeId = typeEntity.pType?.typeId,
                    pTypeName = typeEntity.pType?.typeName,
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
    fun getCIType(typeId: String): CITypeDto {
        val typeDetailEntity = ciTypeRepository.findById(typeId).get()
        return CITypeDto(
            typeId = typeDetailEntity.typeId,
            typeName = typeDetailEntity.typeName,
            typeDesc = typeDetailEntity.typeDesc,
            typeLevel = typeDetailEntity.typeLevel,
            typeAlias = typeDetailEntity.typeAlias,
            pTypeId = typeDetailEntity.pType?.let { typeDetailEntity.pType.typeId },
            pTypeName = typeDetailEntity.pType?.let { typeDetailEntity.pType.typeName!! },
            typeIcon = typeDetailEntity.typeIcon,
            defaultClassId = typeDetailEntity.defaultClass.classId,
            defaultClassName = typeDetailEntity.defaultClass.className
        )
    }

    /**
     *  CMDB Type 등록
     */
    fun createCIType(ciTypeDto: CITypeDto): Boolean {
        var typeLevel = 0
        lateinit var parentTypeEntity: CITypeEntity

        if (ciTypeDto.pTypeId.isNullOrEmpty()) {
            parentTypeEntity = ciTypeRepository.findById(CITypeConstants.CI_TYPE_ROOT_ID).get()
            typeLevel = 0
        } else {
            parentTypeEntity = ciTypeRepository.findById(ciTypeDto.pTypeId!!).get()
            typeLevel = parentTypeEntity.typeLevel!! + 1
        }

        val ciTypeEntity = CITypeEntity(
            pType = parentTypeEntity,
            typeName = ciTypeDto.typeName,
            typeDesc = ciTypeDto.typeDesc,
            typeAlias = ciTypeDto.typeAlias,
            typeIcon = ciTypeDto.typeIcon,
            defaultClass = ciClassRepository.getOne(ciTypeDto.defaultClassId),
            typeLevel = typeLevel
        )

        ciTypeEntity.createUser = ciTypeDto.createUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        ciTypeEntity.createDt = ciTypeDto.createDt

        ciTypeRepository.save(ciTypeEntity)
        return true
    }

    /**
     *  CMDB Type 수정
     */
    fun updateCIType(ciTypeDto: CITypeDto, typeId: String): Boolean {
        val ciTypeEntity = CITypeEntity(
            typeId = ciTypeDto.typeId,
            typeName = ciTypeDto.typeName,
            typeDesc = ciTypeDto.typeDesc,
            typeLevel = ciTypeDto.typeLevel,
            typeIcon = ciTypeDto.typeIcon,
            defaultClass = ciClassRepository.getOne(ciTypeDto.defaultClassId)
        )
        ciTypeEntity.updateUser = ciTypeDto.updateUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        ciTypeEntity.updateDt = ciTypeDto.updateDt
        ciTypeRepository.save(ciTypeEntity)
        return true
    }

    /**
     *  CMDB Type 삭제
     */
    fun deleteCIType(typeId: String): Boolean {
        ciTypeRepository.deleteById(typeId)
        return true
    }
}
