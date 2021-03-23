/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CIClassEntity
import co.brainz.cmdb.provider.dto.CIClassListDto
import co.brainz.cmdb.provider.dto.CIClassToAttributeDto
import co.brainz.cmdb.provider.dto.SearchDto
import co.brainz.framework.querydsl.AliceRepositoryCustom
import com.querydsl.core.QueryResults

interface CIClassRepositoryCustom : AliceRepositoryCustom {
    fun findClass(classId: String): CIClassListDto
    fun findClassList(searchDto: SearchDto): QueryResults<CIClassListDto>
    fun findClassEntityList(search: String): QueryResults<CIClassEntity>
    fun findClassToAttributeList(classList: MutableList<String>): List<CIClassToAttributeDto>?
}
