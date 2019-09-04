package com.brainz.framework.sample.db.controller;

import java.util.Arrays;
import java.util.List;

import com.brainz.framework.sample.db.model.User;
import com.brainz.framework.sample.db.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
 
@RestController
public class UserController {
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/sample/db/init")
	public String init() {		
        // User 여러개 넣기
	    userRepository.saveAll(Arrays.asList(new User("hcjung", "정희찬")
	                   , new User("jje", "정지은")
                       , new User("smkim", "김성민")
                       , new User("hcpark", "박현철")
                       , new User("kbh", "김범호")
                       , new User("wdj", "우다정")
                       , new User("jylim", "임지영")
                       , new User("shlee", "이소현")
                       , new User("hc.jung", "정현규")));
		
		return "데이터가 생성되었습니다.";
	}
	
	@PostMapping("/sample/db/insert")
	public String create(@RequestBody User user) {
        // User 1개 넣기
	    userRepository.save(new User(user.getUserId(), user.getUserName()));

	    return "데이터가 생성되었습니다.";
	}
	
	@GetMapping("/sample/db/findall")
	public List<User> findAll() {
		List<User> users = userRepository.findAll();
		return users;
	}
	
	@RequestMapping("/sample/db/search/{id}")
	public String search(@PathVariable long id) {
		String user = "";
		user = userRepository.findById(id).toString();
		return user;
	}
	
	@RequestMapping("/sample/db/searchByUserId/{userId}")
	public List<User> fetchDataByUserId(@PathVariable String userId) {	
		List<User> users = userRepository.findByUserId(userId);
		return users;
	}
}