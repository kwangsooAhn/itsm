package co.brainz.itsm.role.respository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import javax.persistence.EntityManager
import co.brainz.itsm.role.entity.AuthEntity


@Repository
public interface AuthRepository : JpaRepository<AuthEntity, String> {

	//권한 리스트 조회
	public fun findByOrderByAuthIdAsc(): MutableList<AuthEntity>
	
}