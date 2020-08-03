package co.brainz.itsm.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AuthRepository : JpaRepository<AliceAuthEntity, String> {
    /**
     * 권한 상세 정보 조회
     */
    fun findByAuthId(authId: String): AliceAuthEntity

    /**
     * 권한 전체 목록 조회
     */
    @Query("select a from AliceAuthEntity a where (lower(a.authName) like lower(concat('%', :search, '%')) or lower(a.authDesc) like lower(concat('%', :search, '%'))) order by a.authName asc")
    fun findAuthSearch(search: String?): MutableList<AliceAuthEntity>
}
