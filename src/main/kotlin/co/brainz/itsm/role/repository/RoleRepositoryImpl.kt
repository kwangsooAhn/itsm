package co.brainz.itsm.role.repository

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.QAliceRoleEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class RoleRepositoryImpl: QuerydslRepositorySupport(AliceRoleEntity::class.java), RoleRepositoryCustom {
    override fun findAll(): MutableList<AliceRoleEntity> {
        val table = QAliceRoleEntity.aliceRoleEntity
        return  from(table)
                .fetch()
    }
}
