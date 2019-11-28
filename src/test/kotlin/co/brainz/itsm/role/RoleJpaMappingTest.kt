package co.brainz.itsm.role

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.beans.factory.annotation.Autowired

import org.junit.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import co.brainz.itsm.role.respository.RoleRepository
import co.brainz.itsm.role.respository.AuthRepository
import co.brainz.itsm.role.respository.UserRepository
import co.brainz.itsm.role.respository.UserRoleRepository
import co.brainz.itsm.role.entity.RoleEntity
import co.brainz.itsm.role.entity.UserRoleMapEntity

import java.time.LocalDateTime

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RoleJpaMappingTest {

	@Autowired
	lateinit var roleRepository: RoleRepository

	@Autowired
	lateinit var authRepository: AuthRepository

	@Autowired
	lateinit var userRepository: UserRepository

	@Autowired
	lateinit var userRoleRepository: UserRoleRepository


	@Before
	fun save() {
		var inputDate = LocalDateTime.now()

		roleRepository.save(
			RoleEntity(
				roleId = "2", roleName = "서비스데스크담당자", roleDesc = "역할설명",
				createId = "ksmcreate", createDate = inputDate, updateId = "ksmupdate", updateDate = inputDate
			)
		)
	}

	@After
	fun delete() {
		roleRepository.deleteById("2")
	}


	@Test
	fun Save() {
		var role = roleRepository.findByRoleId("2")
		Assert.assertEquals(role.get(0).roleDesc, "역할설명")
	}

	@Test
	fun getTopRoleList() {
		var list = roleRepository.findByOrderByRoleNameAsc()
		Assert.assertNotNull(list)
	}

	@Test
	fun getAuthList() {
		var list = authRepository.findByOrderByAuthIdAsc()
		Assert.assertNotNull(list)
	}
	
	@Test
	fun getUserList() {
		var list = userRepository.findByOrderByUserIdAsc()
		Assert.assertNotNull(list)
	}

	@Test
	fun getUserRoleMapList() {
		var list  = userRoleRepository.findByRoleIdOrderByRoleIdAsc("2")
		Assert.assertNotNull(list)
	}
	
}
