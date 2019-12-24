package co.brainz.itsm.auth

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import javax.persistence.EntityManager

@Repository
public interface AuthRepository : JpaRepository<AuthEntity, String> {

    /**
     * 권한 리스트 조회
     */
    public fun findByOrderByAuthIdAsc(): MutableList<AuthEntity>

    /**
     * 역할별 권한 조회
     */
    public fun findByAuthIdIn(authId: List<String>): List<AuthEntity>
}