package co.brainz.itsm.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import co.brainz.framework.auth.entity.AliceUrlEntity
import co.brainz.framework.auth.entity.AliceMenuEntity


@Repository
interface AuthRepository : JpaRepository<AliceAuthEntity, String> {
    /**
     * 권한 전체 목록 조회
     */
    fun findByOrderByAuthNameAsc(): MutableList<AliceAuthEntity>
    
    /**
     * 메뉴 리스트 조회
     */
    fun findByOrderByMenuIdAsc(): MutableList<AliceMenuEntity>
    
    /**
     * URL 리스트 조회
     */
    fun findByOrderByUrlIdAsc(): MutableList<AliceUrlEntity>
    
    /**
     * 권한 상세 정보 조회
     */
    fun findByAuthId(authId: String): AliceAuthEntity

}
