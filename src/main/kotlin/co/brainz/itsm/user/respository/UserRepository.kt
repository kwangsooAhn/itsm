package co.brainz.itsm.role.respository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import javax.persistence.EntityManager
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.user.entity.UserEntity

@Repository
public interface UserRepository : JpaRepository<AliceUserEntity, String> {

	//사용자리스트
	public fun findByOrderByUserIdAsc(): MutableList<AliceUserEntity>

	public fun findByUserId(userId: String): MutableList<UserEntity>

}