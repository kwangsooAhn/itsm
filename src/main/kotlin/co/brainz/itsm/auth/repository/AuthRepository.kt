package co.brainz.itsm.auth.repository

import co.brainz.itsm.auth.entity.AuthEntity
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository

@Repository
public interface AuthRepository : JpaRepository<AuthEntity, String> {

    /**
     * 권한 리스트 조회
     */
    public fun findByOrderByAuthIdAsc(): MutableList<AuthEntity>

    /**
     * 역할별 권한 조회
     */
    public fun findByAuthIdIn(authId: Array<String>): List<AuthEntity>
}
