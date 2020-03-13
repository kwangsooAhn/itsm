package co.brainz.framework.timezone

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceTimezoneRepository: JpaRepository<AliceTimezoneEntity,String> {

    fun findAllByOrderByTimezoneIdAsc(): MutableList<AliceTimezoneEntity>
}
