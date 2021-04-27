/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag.repository

import co.brainz.framework.tag.entity.AliceTagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface AliceTagRepository : JpaRepository<AliceTagEntity, String>, AliceTagRepositoryCustom {
    @Transactional
    @Modifying
    @Query(
        "DELETE FROM AliceTagEntity AS tagEntity" +
                " WHERE tagEntity.tagType = :tagType AND tagEntity.targetId = :targetId"
    )
    fun deleteByTargetId(tagType: String, targetId: String)
}
