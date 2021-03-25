/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CIClassEntity
import co.brainz.cmdb.dto.CIClassToAttributeDto
import co.brainz.cmdb.dto.SearchDto
import co.brainz.framework.querydsl.AliceRepositoryCustom
import com.querydsl.core.QueryResults

interface CIClassRepositoryCustom : AliceRepositoryCustom {
    fun findClass(classId: String): CIClassEntity?
    fun findClassList(searchDto: SearchDto): QueryResults<CIClassEntity>
    fun findClassEntityList(search: String): QueryResults<CIClassEntity>
    fun findClassToAttributeList(classList: MutableList<String>): List<CIClassToAttributeDto>?
}
