/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.type.service

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

    fun getCmdbTypes(searchValue: String): List<CmdbTypeListDto> {
        val treeTypeList = mutableListOf<CmdbTypeListDto>()
        var returnList: List<CmdbTypeEntity>
        var count: Long
        val queryResults: QueryResults<CmdbTypeEntity> = typeRepository.findByTypeList(searchValue)
        var typeSearchList = queryResults.results
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
                    ptypeId = typeEntity.ptypeId,
                    typeIcon = typeEntity.typeIcon,
                    totalCount = count
                )
            )
        }
        return treeTypeList
    }
}
