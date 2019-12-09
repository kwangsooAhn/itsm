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

import co.brainz.itsm.settings.role.RoleRepository
import co.brainz.itsm.settings.auth.AuthRepository
import co.brainz.itsm.settings.user.UserRepository
import co.brainz.itsm.settings.role.RoleEntity
import co.brainz.itsm.settings.user.UserEntity
import co.brainz.itsm.settings.auth.AuthEntity

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

    @Before
    fun save() {
        var inputDate = LocalDateTime.now()

        var userId1 = "kbh"
        var userId2 = "admin"
        val user1 = userRepository.findByUserId(userId1)
        val user2 = userRepository.findByUserId(userId2)
        val auth1 = AuthEntity("notice.read")
        val auth2 = AuthEntity("notice.create")

        roleRepository.save(
            RoleEntity(
                roleId = "2", roleName = "서비스데스크담당자", roleDesc = "역할설명",
                createId = "ksmcreate", createDate = inputDate, updateId = "ksmupdate", updateDate = inputDate,
                userEntityList = listOf(user1, user2), authEntityList = listOf(auth1, auth2)
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
}
