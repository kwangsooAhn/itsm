package co.brainz.itsm.role

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.beans.factory.annotation.Autowired

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import java.time.LocalDateTime

import co.brainz.itsm.role.entity.RoleEntity
import co.brainz.itsm.role.respository.ITSMRoleRepository
import org.junit.After
import co.brainz.itsm.role.respository.ITSMRoleAuthMapRepository

@RunWith (SpringJUnit4ClassRunner::class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RoleJpaMappingTest {

	@Autowired
	private lateinit var roleRepository: ITSMRoleRepository

	@Autowired
	private lateinit var roleAuthRepository: ITSMRoleAuthMapRepository

	
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
	
	/*
	 * @Test fun Save() { var role = roleRepository.findById("2")
	 * Assert.assertEquals(role.get().roleDesc, "역할설명") }
	 * 
	 * @Test fun AllSelect() { var list =
	 * roleRepository.findAllByOrderByRoleNameAsc() Assert.assertNotNull(list) }
	 */
	
	@Test fun Save() {
		println(roleAuthRepository.findAll())
	}
}
