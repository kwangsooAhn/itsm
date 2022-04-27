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
import com.querydsl.core.QueryResults
import org.springframework.data.domain.Page

interface CIClassRepositoryCustom : AliceRepositoryCustom {
    fun findClass(classId: String): CIClassListDto?
    fun findClassList(searchDto: SearchDto): Page<CIClassListDto>
    fun findClassEntityList(search: String): List<CIClassEntity>
    fun findClassToAttributeList(classList: MutableList<String>): List<CIClassToAttributeDto>?
}
