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
import co.brainz.cmdb.dto.CITypeDto
import co.brainz.cmdb.dto.CITypeListDto
import co.brainz.cmdb.dto.CITypeReturnDto
import co.brainz.cmdb.dto.CITypeTreeListDto
import co.brainz.cmdb.dto.SearchDto
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.fileTransaction.constants.FileConstants
import co.brainz.framework.fileTransaction.provider.AliceFileProvider
import co.brainz.itsm.cmdb.ciType.dto.CITypeTreeReturnDto
import com.querydsl.core.QueryResults
import java.io.File
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CITypeService(
    private val ciTypeRepository: CITypeRepository,
    private val ciClassRepository: CIClassRepository,
    private val aliceUserRepository: AliceUserRepository,
    private val aliceFileProvider: AliceFileProvider
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CMDB Type 목록 조회
     */
    fun getCITypes(parameters: LinkedHashMap<String, Any>): CITypeReturnDto {
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
        val ciTypes = ciTypeRepository.findTypeList(searchDto)
        return CITypeReturnDto(
            data = ciTypes.results,
            totalCount = ciTypes.total
        )
    }

    /**
     * CMDB Type 단일 목록 조회
     */
    fun getCIType(typeId: String): CITypeListDto? {
        return ciTypeRepository.findType(typeId)
    }

    /**
     *  CMDB Type 트리 조회
     */
    fun getCITypesTree(parameters: LinkedHashMap<String, Any>): CITypeTreeReturnDto {
        var search = ""
        if (parameters["search"] != null) search = parameters["search"].toString()
        val treeTypeList = mutableListOf<CITypeTreeListDto>()
        val queryResults: QueryResults<CITypeEntity> = ciTypeRepository.findByTypeList(search)
        val returnList: List<CITypeEntity>
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
        val count: Long = queryResults.total
        returnList = typeSearchList

        for (typeEntity in returnList) {
            treeTypeList.add(
                CITypeTreeListDto(
                    typeId = typeEntity.typeId,
                    typeName = typeEntity.typeName,
                    typeDesc = typeEntity.typeDesc,
                    typeLevel = typeEntity.typeLevel,
                    typeAlias = typeEntity.typeAlias,
                    pTypeId = typeEntity.pType?.typeId,
                    pTypeName = typeEntity.pType?.typeName,
                    typeIcon = typeEntity.typeIcon,
                    typeIconData = typeEntity.typeIcon?.let { getCITypeImageData(typeEntity.typeIcon) },
                    defaultClassId = typeEntity.defaultClass.classId,
                    defaultClassName = typeEntity.defaultClass.className
                )
            )
        }
        return CITypeTreeReturnDto(
            data = treeTypeList,
            totalCount = count
        )
    }

    /**
     *  CMDB Type 상세 조회
     */
    fun getCITypeDetail(typeId: String): CITypeDto {
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
            typeIconData = typeDetailEntity.typeIcon?.let { getCITypeImageData(typeDetailEntity.typeIcon) },
            defaultClassId = typeDetailEntity.defaultClass.classId,
            defaultClassName = typeDetailEntity.defaultClass.className,
            createDt = typeDetailEntity.createDt,
            createUserKey = typeDetailEntity.createUser?.userKey,
            updateDt = typeDetailEntity.updateDt,
            updateUserKey = typeDetailEntity.updateUser?.userKey
        )
    }

    /**
     *  CMDB Type 등록
     */
    fun createCIType(ciTypeDto: CITypeDto): Boolean {
        val typeLevel: Int
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
    fun updateCIType(typeId: String, ciTypeDto: CITypeDto): Boolean {
        val parentTypeEntity: CITypeEntity = ciTypeRepository.findById(ciTypeDto.pTypeId!!).get()

        val ciTypeEntity = CITypeEntity(
            typeId = ciTypeDto.typeId,
            typeName = ciTypeDto.typeName,
            typeDesc = ciTypeDto.typeDesc,
            typeAlias = ciTypeDto.typeAlias,
            typeLevel = ciTypeDto.typeLevel,
            pType = parentTypeEntity,
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

    /**
     * CI Type 아이콘 파일 읽어오기
     *
     * @param ciTypeIconName 저장되어 있는 CI Type 아이콘 파일 이름
     * @return String 데이터화된 아이콘 이미지
     */
    fun getCITypeImageData(ciTypeIconName: String): String {
        return aliceFileProvider.getDataUriSchema(FileConstants.Path.ICON_CI_TYPE.path + File.separator + ciTypeIconName)
    }
}
