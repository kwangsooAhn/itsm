package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity

interface UserRepositoryCustom {
    fun findAliceUserEntityList(search: String, category: String): List<AliceUserEntity>
}
