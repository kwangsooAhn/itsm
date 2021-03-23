/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciType.repository

import co.brainz.cmdb.ciType.entity.CITypeEntity
import co.brainz.cmdb.provider.dto.CITypeListDto
import co.brainz.cmdb.provider.dto.SearchDto
import co.brainz.framework.querydsl.AliceRepositoryCustom
import com.querydsl.core.QueryResults

interface CITypeRepositoryCustom : AliceRepositoryCustom {

    fun findType(typeId: String): CITypeListDto
    fun findTypeList(searchDto: SearchDto): QueryResults<CITypeListDto>
    fun findByTypeList(search: String): QueryResults<CITypeEntity>
    fun findByCITypeAll(): List<CITypeEntity>?
}
