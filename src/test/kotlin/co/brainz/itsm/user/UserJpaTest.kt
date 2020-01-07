package co.brainz.itsm.user

import co.brainz.framework.encryption.JasyptConfig
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime
import java.util.*

@RunWith(SpringRunner::class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableEncryptableProperties
@Import(JasyptConfig::class)
class UserJpaTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var userEntity: UserEntity
    private lateinit var userUpdateDto: UserUpdateDto

    @Before
    fun setUp() {
        userEntity = UserEntity(
            UUID.randomUUID().toString(), "kbh", "itsm123", "kbh", "kbh@brainz.co.kr", true,
            0, "과장", "ITSM팀", "02-6416-8324", "admin",
            "admin", "status", "code", "", LocalDateTime.now(), LocalDateTime.now(),
            LocalDateTime.now(), emptySet()
        )

        userUpdateDto = UserUpdateDto(
            "kbh", "beom", "kbh@brainz.co.kr", "대리", "ITSM팀",
            "02-6416-8324", "010-0000-1234", emptySet(), "code",
            "status"
        )

    }

    @Test
    fun 사용자_업데이트() {
        val targetEntity = userEntity
        targetEntity.userName = "beom"

        val rsltEntity = userRepository.save(targetEntity)

        Assert.assertThat(rsltEntity.userName, CoreMatchers.`is`(CoreMatchers.equalTo("beom")))
    }
}
