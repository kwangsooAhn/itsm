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
import co.brainz.cmdb.constants.RestTemplateConstants
import co.brainz.cmdb.dto.CIAttributeValueDto
import co.brainz.cmdb.dto.CIAttributeValueGroupListDto
import co.brainz.cmdb.dto.CIClassDetailDto
import co.brainz.cmdb.dto.CIClassDetailValueDto
import co.brainz.cmdb.dto.CIClassDto
import co.brainz.cmdb.dto.CIClassListDto
import co.brainz.cmdb.dto.CIClassReturnDto
import co.brainz.cmdb.dto.CIClassToAttributeDto
import co.brainz.cmdb.dto.CIClassTreeListDto
import co.brainz.cmdb.dto.SearchDto
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.itsm.cmdb.ciClass.dto.CIClassTreeReturnDto
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.querydsl.core.QueryResults
import javax.transaction.Transactional
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
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

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
    fun getCIClass(classId: String): CIClassListDto? {
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
        val attributes = ciClassRepository.findClassToAttributeList(classList)
        var extendsAttributes: List<CIClassToAttributeDto>? = null

        val pClassName = ciClassEntity.pClass?.className

        if (ciClassRepository.existsByPClass(
                ciClassRepository.findById(classId).orElse(CIClassEntity(classId = classId))
            )
        ) {
            editable = false
        }

        val recursiveClassList = mutableListOf<String>()
        if (ciClassEntity.pClass != null) {
            val ciClassResult = ciClassRepository.findClassList(SearchDto(offset = null, limit = null))
            recursiveClassList.addAll(
                getRecursiveParentClassId(
                    ciClassResult.results,
                    ciClassEntity.pClass?.classId,
                    mutableListOf()
                )
            )
        }

        if (recursiveClassList.isNotEmpty()) {
            extendsAttributes = ciClassRepository.findClassToAttributeList(recursiveClassList)
        }

        return CIClassDetailDto(
            classId = ciClassEntity.classId,
            className = ciClassEntity.className,
            classDesc = ciClassEntity.classDesc,
            classSeq = ciClassEntity.classSeq,
            pClassId = ciClassEntity.pClass?.classId,
            pClassName = pClassName,
            editable = editable,
            attributes = attributes,
            extendsAttributes = extendsAttributes
        )
    }

    /**
     * CI Class 부모가 존재할 경우 최상위까지 조회화여 classId 추출
     */
    private fun getRecursiveParentClassId(
        allCIClassDtoList: List<CIClassListDto>,
        pClassId: String?,
        recursiveClassList: MutableList<String>
    ): List<String> {
        if (pClassId != null) {
            recursiveClassList.add(pClassId)
            for (ciClassListDto in allCIClassDtoList) {
                if (ciClassListDto.classId == pClassId) {
                    getRecursiveParentClassId(allCIClassDtoList, ciClassListDto.pClassId, recursiveClassList)
                }
            }
        }
        return recursiveClassList.toList()
    }

    /**
     * CMDB CI Class 트리 조회
     */
    fun getCIClassesTree(parameters: LinkedHashMap<String, Any>): CIClassTreeReturnDto {
        var search = ""
        if (parameters["search"] != null) search = parameters["search"].toString()

        val treeClassList = mutableListOf<CIClassTreeListDto>()
        val queryResults: QueryResults<CIClassEntity> = ciClassRepository.findClassEntityList(search)
        val returnList: List<CIClassEntity>
        val pClassList = mutableListOf<CIClassEntity>()
        val classIdList = mutableListOf<String>()
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

        val count: Long = queryResults.total
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
                    totalAttributes = attributeCount
                )
            )
        }
        return CIClassTreeReturnDto(
            data = treeClassList,
            totalCount = count
        )
    }

    /**
     * CMDB CI Class 저장
     */
    @Transactional
    fun createCIClass(ciClassDto: CIClassDto): Boolean {
        var order = 1
        val ciClassEntity = CIClassEntity(
            classId = ciClassDto.classId,
            className = ciClassDto.className,
            classDesc = ciClassDto.classDesc,
            classSeq = ciClassDto.classSeq,
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
    @Transactional
    fun updateCIClass(ciClassDto: CIClassDto): Boolean {
        var order = 1
        val ciClassEntity = ciClassRepository.findByIdOrNull(ciClassDto.classId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[CMDB CLASS Entity]"
        )
        ciClassEntity.className = ciClassDto.className
        ciClassEntity.classDesc = ciClassDto.classDesc
        ciClassEntity.classSeq = ciClassDto.classSeq
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
    @Transactional
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
    fun getCIClassAttributes(ciId: String, classId: String): List<CIClassDetailValueDto> {
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
            val queryResult = ciAttributeRepository.findAttributeValueList(ciId, it)
            val ciAttributeGroupList = mutableListOf<CIAttributeValueGroupListDto>()
            for (data in queryResult.results) {
                var childAttributeList = mutableListOf<CIAttributeValueDto>()
                if (data.attributeType.equals(RestTemplateConstants.AttributeType.GROUP_LIST.code)) {
                    val attributeValue = mapper.readValue(data.attributeValue, LinkedHashMap::class.java)
                    val attributeOptions: List<Map<String, Any>> = mapper.convertValue(attributeValue["option"],
                        object : TypeReference<List<Map<String, Any>>>() {})
                    childAttributeList = this.getChildAttributesForGroupList(data.attributeId, ciId, attributeOptions)
                }
                ciAttributeGroupList.add(
                    CIAttributeValueGroupListDto(
                        attributeId = data.attributeId,
                        attributeName = data.attributeName,
                        attributeText = data.attributeText,
                        attributeType = data.attributeType,
                        attributeOrder = data.attributeOrder,
                        attributeValue = data.attributeValue,
                        value = data.value,
                        childAttributes = childAttributeList
                    )
                )
            }
            val ciClassDetailValueDto = CIClassDetailValueDto(
                className = ciClassRepository.findClass(it)?.className,
                attributes = ciAttributeGroupList
            )
            attributeValueAll.add(ciClassDetailValueDto)
        }
        return attributeValueAll.toList()
    }

    /**
     * Group List에 포함된 CI 세부 속성 조회
     */
    private fun getChildAttributesForGroupList(
        attributeId: String,
        ciId: String,
        options: List<Map<String, Any>>
    ): MutableList<CIAttributeValueDto> {
        val attributeIdList = mutableListOf<String>()
        for (option in options) {
            attributeIdList.add(option["id"] as String)
        }
        val ciAttributeDataList = mutableListOf<CIAttributeValueDto>()
        // 1. cmdb_attribute 테이블 데이터 조회
        val ciAttributeQueryResult = ciAttributeRepository.findAttributeListInGroupList(attributeIdList)
        // 2. cmdb_ci_group_list_data 테이블 데이터가 조회
        val groupListQueryResult = ciAttributeRepository.findGroupListData(attributeId, ciId)
        // 3. 데이터가 존재하면 데이터 병합
        if (groupListQueryResult.total > 0) {
            for (groupData in groupListQueryResult.results) {
                loop@ for (attribute in ciAttributeQueryResult.results) {
                    if (groupData.cAttributeId == attribute.attributeId) {
                        ciAttributeDataList.add(
                            CIAttributeValueDto(
                                attributeId = groupData.cAttributeId,
                                attributeName = attribute.attributeName,
                                attributeText = attribute.attributeText,
                                attributeType = attribute.attributeType,
                                attributeOrder = groupData.cAttributeSeq,
                                attributeValue = attribute.attributeValue,
                                value = groupData.cValue
                            )
                        )
                        break@loop
                    }
                }
            }
        } else {
            // 4. 데이터가 없으면 cmdb_attribute 테이블의 데이터를 이용하여 세부속성 첫번째 그룹을 담음.
            for (attribute in ciAttributeQueryResult.results) {
                ciAttributeDataList.add(attribute)
            }
        }
        return ciAttributeDataList
    }
}
