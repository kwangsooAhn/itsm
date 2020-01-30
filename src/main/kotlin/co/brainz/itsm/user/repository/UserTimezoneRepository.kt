package co.brainz.itsm.user.repository

import co.brainz.itsm.user.entity.UserTimezoneEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserTimezoneRepository: JpaRepository<UserTimezoneEntity,String> {

    public fun findAllByOrderByTimezoneIdAsc(): MutableList<UserTimezoneEntity>
}