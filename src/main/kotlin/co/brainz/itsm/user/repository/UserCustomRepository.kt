package co.brainz.itsm.user.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.user.dto.UserCustomDto
import co.brainz.itsm.user.entity.UserCustomEntity
import co.brainz.itsm.user.entity.UserCustomEntityPk
import javax.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository

interface UserCustomRepository : JpaRepository<UserCustomEntity, UserCustomEntityPk> {
    fun findByUserAndCustomType(user: AliceUserEntity, customType: String): MutableList<UserCustomDto>

    @Transactional
    fun deleteByUserAndAndCustomType(user: AliceUserEntity, customType: String): Int
}