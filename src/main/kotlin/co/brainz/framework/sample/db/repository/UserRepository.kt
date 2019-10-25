package co.brainz.framework.sample.db.repository;

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import co.brainz.framework.sample.db.model.User

@Repository
public interface UserRepository : CrudRepository<User, Long> {
    fun findByUserId(userId : String) : MutableList<User>
    override fun findAll() : MutableList<User> 
}
