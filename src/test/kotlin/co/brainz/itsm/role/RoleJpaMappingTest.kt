package co.brainz.itsm.role

import co.brainz.itsm.role.entity.RoleEntity
import co.brainz.itsm.role.respository.RoleRepository

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@DataJpaTest(showSql = true)
class RoleJpaMappingTest {
	
	@Autowired
	private lateinit var roleRepository: RoleRepository

	@Autowired
	lateinit var roleEntity: RoleEntity

	@Before
	fun init() {
		roleRepository.save(
			RoleEntity(
				roleNo = 2,
				roleName = "서비스데스크담당자",
				roleDesc = "테스트야",
				createId = "ksm",
				createDate = "2019-12-12",
				updateId = null,
				updateDate = null
			)
		)
	}

	@Test
	fun saveTest() {
		var RoleEntity = roleRepository.findById(2)
		print(RoleEntity.get().roleDesc)
	}
}
