package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceUrlEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceUrlRepository : JpaRepository<AliceUrlEntity, String>, AliceUrlRepositoryCustom {
    fun findByOrderByMethodAscUrlAsc(): MutableList<AliceUrlEntity>
    fun findAliceUrlEntityByRequiredAuthIs(isRequiredAuth: Boolean): List<AliceUrlEntity>
}
