/*
package co.brainz.framework.sample.db.controller

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.brainz.framework.sample.db.model.User_Kotlin
import co.brainz.framework.sample.db.repository.UserRepository_Kotlin

@RestController
public class UserController_Kotlin{
	@Autowired
	lateinit var userRepository : UserRepository_Kotlin

	@GetMapping("/sample/db/init")
	public fun init() : String {

		userRepository.save(User_Kotlin("hcjung","정희찬"))
		userRepository.save(User_Kotlin("jje","정지은"))
		userRepository.save(User_Kotlin("smkim","김성민"))
		userRepository.save(User_Kotlin("hcpark","박현철"))
		userRepository.save(User_Kotlin("kbh","김범호"))
		userRepository.save(User_Kotlin("wdj","우다정"))
		userRepository.save(User_Kotlin("jylim","임지영"))
		userRepository.save(User_Kotlin("shlee","이소현"))
		userRepository.save(User_Kotlin("hc.jung","정현규"))
		userRepository.save(User_Kotlin("Kotlin","코틀린"))
				
		return "데이터가 생성되었습니다. ( based on Kotlin )"
	}
	
	@PostMapping("/sample/db/insert")
	public fun create(@RequestBody user : User_Kotlin) : String {
		// User 1개 넣기
		userRepository.save(User_Kotlin(user.userId, user.userName))
		
		
		return "데이터가 생성되었습니다 ( based on Kotlin )."
	}

	@GetMapping("/sample/db/findall")
    public fun findAll() : MutableList<User_Kotlin> {
		val users : MutableList<User_Kotlin> = userRepository.findAll()
		return users
	}
	
	@RequestMapping("/sample/db/search/{id}")
    public fun search(@PathVariable id : Long) : String {
		val user : String = userRepository.findById(id).toString()
		return user
	}
	
	@RequestMapping("/sample/db/searchByUserId/{userId}")
    public fun fetchDataByUserId(@PathVariable userId : String ) : MutableList<User_Kotlin> {
		val users : MutableList<User_Kotlin> = userRepository.findByUserId(userId) 
		return users
	}
}
*/