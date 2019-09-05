package co.brainz.framework.sample.db.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import co.brainz.framework.sample.db.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	List<User> findByUserId(String userId);
	List<User> findAll();
}