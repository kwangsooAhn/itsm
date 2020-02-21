package co.brainz.itsm.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository


@Repository
interface AuthRepository : JpaRepository<AliceAuthEntity, String> {
    /**
     * 권한 전체 목록 조회
     */
    fun findByOrderByAuthNameAsc(): MutableList<AliceAuthEntity>
    
    /**
     * 권한 상세 정보 조회
     */
    fun findByAuthId(authId: String): AliceAuthEntity

}
