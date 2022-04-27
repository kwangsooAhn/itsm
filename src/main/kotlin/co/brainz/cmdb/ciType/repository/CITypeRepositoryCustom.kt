/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciType.repository

import co.brainz.cmdb.ciType.entity.CITypeEntity
import co.brainz.cmdb.dto.CITypeListDto
import co.brainz.cmdb.dto.SearchDto
import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto

interface CITypeRepositoryCustom : AliceRepositoryCustom {
    fun findType(typeId: String): CITypeListDto?
    fun findTypeList(searchDto: SearchDto): PagingReturnDto
    fun findByTypeList(search: String): List<CITypeEntity>
    fun findByCITypeAll(): List<CITypeEntity>?
    fun findCITypeByTypeName(typeName: String): CITypeEntity?
}
