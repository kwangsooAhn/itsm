package com.brainz.framework.sample.db.repository;

import java.util.List;

import com.brainz.framework.sample.db.model.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	List<User> findByUserId(String userId);
	List<User> findAll();
}