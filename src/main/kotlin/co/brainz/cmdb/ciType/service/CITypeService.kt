/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciType.service

import co.brainz.cmdb.ciType.entity.CmdbTypeEntity
import co.brainz.cmdb.ciType.repository.CITypeRepository
import co.brainz.cmdb.provider.dto.CmdbTypeDto
import co.brainz.cmdb.provider.dto.CmdbTypeListDto
import com.querydsl.core.QueryResults
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CITypeService(
    private val CITypeRepository: CITypeRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     *  CMDB Type 트리 조회.
     */
    fun getCmdbTypes(searchValue: String): List<CmdbTypeListDto> {
        val treeTypeList = mutableListOf<CmdbTypeListDto>()
        val queryResults: QueryResults<CmdbTypeEntity> = CITypeRepository.findByTypeList(searchValue)
        var returnList: List<CmdbTypeEntity>
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
                    defaultClassId = typeEntity.defaultClassId,
                    ptypeId = typeEntity.pType?.typeId,
                    ptypeName = typeEntity.pType?.typeName,
                    typeIcon = typeEntity.typeIcon,
                    totalCount = count
                )
            )
        }
        return treeTypeList
    }

    /**
     *  CMDB Type 단일 조회.
     */
    fun getCmdbType(typeId: String): CmdbTypeDto {
        return CITypeRepository.findByTypeId(typeId)
    }
}
