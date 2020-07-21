/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.avatar.repository

import co.brainz.framework.avatar.entity.AliceAvatarEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceAvatarRepository : JpaRepository<AliceAvatarEntity, String> {
    fun findByAvatarId(avatarId: String): AliceAvatarEntity
}
