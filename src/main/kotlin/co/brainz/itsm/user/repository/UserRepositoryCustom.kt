package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import com.querydsl.core.QueryResults

interface UserRepositoryCustom {
    fun findAliceUserEntityList(search: String, category: String, offset: Long): QueryResults<AliceUserEntity>
}
