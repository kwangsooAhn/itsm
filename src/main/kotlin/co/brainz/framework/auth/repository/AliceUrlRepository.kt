package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceUrlEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
@Repository
interface AliceUrlRepository : JpaRepository<AliceUrlEntity, String> {
    fun findByOrderByUrlAsc(): MutableList<AliceUrlEntity>

    fun findByUrlIn(Urls: List<String>): List<AliceUrlEntity>
}