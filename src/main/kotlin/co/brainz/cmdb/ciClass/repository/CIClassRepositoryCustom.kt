/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CIClassEntity
import co.brainz.cmdb.dto.CIClassListDto
import co.brainz.cmdb.dto.CIClassToAttributeDto
import co.brainz.cmdb.dto.SearchDto
import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto

interface CIClassRepositoryCustom : AliceRepositoryCustom {
    fun findClass(classId: String): CIClassListDto?
    fun findClassList(searchDto: SearchDto): PagingReturnDto
    fun findClassEntityList(search: String): List<CIClassEntity>
    fun findClassToAttributeList(classList: MutableList<String>): List<CIClassToAttributeDto>?
}
