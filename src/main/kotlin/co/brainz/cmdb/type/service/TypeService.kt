/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.type.service

import co.brainz.cmdb.provider.dto.CmdbTypeDto
import co.brainz.cmdb.provider.dto.CmdbTypeListDto
import co.brainz.cmdb.type.entity.CmdbTypeEntity
import co.brainz.cmdb.type.repository.TypeRepository
import com.querydsl.core.QueryResults
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TypeService(
    private val typeRepository: TypeRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     *  CMDB Type 트리 조회.
     */
    fun getCmdbTypes(searchValue: String): List<CmdbTypeListDto> {
        val treeTypeList = mutableListOf<CmdbTypeListDto>()
        val queryResults: QueryResults<CmdbTypeEntity> = typeRepository.findByTypeList(searchValue)
        var returnList: List<CmdbTypeEntity>
        var count = 0L
        var typeSearchList = queryResults.results

        val pTypeList = mutableListOf<CmdbTypeEntity>()
        for (type in typeSearchList) {
            var tempType = type.ptypeId
            do {
                if (tempType != null) {
                    pTypeList.add(tempType)
                    tempType = tempType.ptypeId
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
                    ptypeId = typeEntity.ptypeId?.typeId,
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
        val typeEntity = typeRepository.findTypeEntityByTypeId(typeId).get()

        return CmdbTypeDto(
            typeId = typeEntity.typeId,
            typeName = typeEntity.typeName,
            typeDesc = typeEntity.typeDesc,
            defaultClassId = typeEntity.defaultClassId,
            ptypeId = typeEntity.ptypeId!!.typeName,
            typeIcon = typeEntity.typeIcon
        )
    }
}
