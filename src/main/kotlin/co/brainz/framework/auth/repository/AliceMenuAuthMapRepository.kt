package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceMenuAuthMapEntity
import co.brainz.framework.auth.entity.AliceMenuAuthMapPk
import org.springframework.data.jpa.repository.JpaRepository

interface AliceMenuAuthMapRepository: JpaRepository<AliceMenuAuthMapEntity, AliceMenuAuthMapPk> {
    fun findByAuth(AuthInfo : AliceAuthEntity): MutableList<AliceMenuAuthMapEntity>
}
