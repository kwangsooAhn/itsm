/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceMenuEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceMenuRepository : JpaRepository<AliceMenuEntity, String>, AliceMenuRepositoryCustom {
    fun findByOrderByMenuIdAsc(): MutableList<AliceMenuEntity>
    fun findByMenuIdIn(menuIds: List<String>): List<AliceMenuEntity>
}
