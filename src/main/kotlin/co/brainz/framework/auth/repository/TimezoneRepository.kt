package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.TimezoneEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TimezoneRepository: JpaRepository<TimezoneEntity,String> {

    fun findAllByOrderByTimezoneIdAsc(): MutableList<TimezoneEntity>
}