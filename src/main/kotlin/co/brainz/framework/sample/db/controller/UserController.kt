package co.brainz.framework.sample.db.controller

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.brainz.framework.sample.db.model.User
import co.brainz.framework.sample.db.repository.UserRepository

@RestController
public class UserController{
	@Autowired
	lateinit var userRepository : UserRepository

	@GetMapping("/sample/db/init")
	public fun init() : String {

		userRepository.save(User("hcjung","정희찬"))
		userRepository.save(User("jje","정지은"))
		userRepository.save(User("smkim","김성민"))
		userRepository.save(User("hcpark","박현철"))
		userRepository.save(User("kbh","김범호"))
		userRepository.save(User("wdj","우다정"))
		userRepository.save(User("jylim","임지영"))
		userRepository.save(User("shlee","이소현"))
		userRepository.save(User("hc.jung","정현규"))
		userRepository.save(User("Kotlin","코틀린"))
				
		return "데이터가 생성되었습니다."
	}
	
	@PostMapping("/sample/db/insert")
	public fun create(@RequestBody user : User) : String {
		// User 1개 넣기
		userRepository.save(User(user.userId, user.userName))
		
		
		return "데이터가 생성되었습니다."
	}

	@GetMapping("/sample/db/findall")
    public fun findAll() : MutableList<User> {
		val users : MutableList<User> = userRepository.findAll()
		return users
	}
	
	@RequestMapping("/sample/db/search/{id}")
    public fun search(@PathVariable id : Long) : String {
		val user : String = userRepository.findById(id).toString()
		return user
	}
	
	@RequestMapping("/sample/db/searchByUserId/{userId}")
    public fun fetchDataByUserId(@PathVariable userId : String ) : MutableList<User> {
		val users : MutableList<User> = userRepository.findByUserId(userId) 
		return users
	}
}


