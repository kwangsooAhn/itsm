package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AliceAuthRepository: JpaRepository<AliceAuthEntity, String> {
    /**
     * 권한 리스트 조회
     */
    public fun findByOrderByAuthIdAsc(): MutableList<AliceAuthEntity>

    /**
     * 역할별 권한 조회
     */
    public fun findByAuthIdIn(roleId: MutableSet<String>): MutableSet<AliceAuthEntity>
}
