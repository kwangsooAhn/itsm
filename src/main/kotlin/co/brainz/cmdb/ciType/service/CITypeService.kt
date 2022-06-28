/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciType.service

import co.brainz.cmdb.ciClass.entity.CIClassEntity
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
import co.brainz.itsm.cmdb.ciType.constants.CITypeConstants.Status
import co.brainz.itsm.cmdb.ciType.dto.CITypeTreeReturnDto
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
            data = ciTypes.dataList as List<CITypeListDto>,
            totalCount = ciTypes.totalCount
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
        val results = ciTypeRepository.findByTypeList(search)
        val returnList: List<CITypeEntity>
        var typeSearchList = results
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
            typeSearchList += pTypeList
            typeSearchList = typeSearchList.distinct()
        }
        val count: Long = results.size.toLong()
        returnList = typeSearchList

        for (typeEntity in returnList) {
            treeTypeList.add(
                CITypeTreeListDto(
                    typeId = typeEntity.typeId,
                    typeName = typeEntity.typeName,
                    typeDesc = typeEntity.typeDesc,
                    typeLevel = typeEntity.typeLevel,
                    typeSeq = typeEntity.typeSeq,
                    typeAlias = typeEntity.typeAlias,
                    pTypeId = typeEntity.pType?.typeId,
                    pTypeName = typeEntity.pType?.typeName,
                    typeIcon = typeEntity.typeIcon,
                    typeIconData = typeEntity.typeIcon?.let { getCITypeImageData(typeEntity.typeIcon!!) },
                    classId = typeEntity.ciClass.classId,
                    className = typeEntity.ciClass.className
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
        var editable = true

        if (ciTypeRepository.existsByPType(
                ciTypeRepository.findById(typeId).orElse(CITypeEntity(typeId = typeId, ciClass = CIClassEntity()))
            )
        ) {
            editable = false
        }

        return CITypeDto(
            typeId = typeDetailEntity.typeId,
            typeName = typeDetailEntity.typeName,
            typeDesc = typeDetailEntity.typeDesc,
            typeLevel = typeDetailEntity.typeLevel,
            typeSeq = typeDetailEntity.typeSeq,
            typeAlias = typeDetailEntity.typeAlias,
            pTypeId = typeDetailEntity.pType?.typeId,
            pTypeName = typeDetailEntity.pType?.typeName,
            typeIcon = typeDetailEntity.typeIcon,
            typeIconData = typeDetailEntity.typeIcon?.let { getCITypeImageData(typeDetailEntity.typeIcon!!) },
            classId = typeDetailEntity.ciClass.classId,
            className = typeDetailEntity.ciClass.className,
            createDt = typeDetailEntity.createDt,
            createUserKey = typeDetailEntity.createUser?.userKey,
            updateDt = typeDetailEntity.updateDt,
            updateUserKey = typeDetailEntity.updateUser?.userKey,
            editable = editable
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
            typeSeq = ciTypeDto.typeSeq,
            typeAlias = ciTypeDto.typeAlias,
            typeIcon = ciTypeDto.typeIcon,
            ciClass = ciClassRepository.getOne(ciTypeDto.classId),
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
            typeSeq = ciTypeDto.typeSeq,
            pType = parentTypeEntity,
            typeIcon = ciTypeDto.typeIcon,
            ciClass = ciClassRepository.getOne(ciTypeDto.classId)
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
        // todo #10536 아이콘 선택값이 없을 경우 기본 아이콘 처리
        return when (ciTypeIconName != "") {
            true -> aliceFileProvider.getDataUriSchema(
                FileConstants.Path.ICON_CI_TYPE.path + File.separator + ciTypeIconName
            )
            false -> ""
        }
    }

    /**
     * CI Class로 CI Type이 있는지 확인
     *
     * @param classId
     * @return Boolean CI Type 있을 경우 True, 없을 경우 False
     */
    fun getCITypesByClassId(classId: String): Boolean {
        return ciTypeRepository.existsCITypeEntitiesByCiClass_ClassId(classId)
    }

    /**
     * Type Name으로 CI Type Entity 조회
     *
     * @param typeName
     * @return CITypeEntity?
     */
    fun getCITypeByTypeName(typeName: String): CITypeEntity? {
        return ciTypeRepository.findCITypeByTypeName(typeName)
    }

    /**
     * CI Type로 중복체크
     *
     * @param ciTypeDto
     * @return String CI TYPE 중복에 따른 코드값
     */
    fun checkValidation(ciTypeDto: CITypeDto): String {
        val preCITypeEntity = ciTypeRepository.findByTypeId(ciTypeDto.typeId) // 수정전 CI Type 정보

        return if (ciTypeRepository.existsByTypeAlias(ciTypeDto.typeAlias!!) &&
            ciTypeDto.typeAlias != preCITypeEntity.typeAlias
        ) { // 식별자 중복체크 && 수정 전후 식별자(TypeAlias) 동일건은 예외
            Status.STATUS_FAIL_TYPE_ALIAS_DUPLICATION.code
        } else if (ciTypeRepository.existsByPTypeAndTypeName(
                ciTypeRepository.findByTypeId(ciTypeDto.pTypeId!!), ciTypeDto.typeName!!
            ) &&
            ciTypeDto.typeName != preCITypeEntity.typeName
        ) { // 부모유형 + 유형이름 중복체크 && 수정 전후 유형이름(TypeName) 동일한건은 예외
            Status.STATUS_FAIL_PTYPE_AND_TYPENAME_DUPLICATION.code
        } else {
            Status.STATUS_SUCCESS.code
        }
    }
}
