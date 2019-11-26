package co.brainz.itsm.role.respository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
//import co.brainz.itsm.role.entity.UserEntity
import org.springframework.data.repository.CrudRepository
import javax.persistence.EntityManager
import co.brainz.framework.auth.entity.UserEntity

@Repository
public interface ITSMUserRepository : JpaRepository<UserEntity, String> {

	//사용자리스트
	public fun findByOrderByUserIdAsc(): MutableList<UserEntity>
}