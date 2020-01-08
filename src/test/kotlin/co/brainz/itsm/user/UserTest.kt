package co.brainz.itsm.user

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class UserTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean(name = "userService")
    private lateinit var userService: UserService

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

        val userSearchDto = UserSearchDto(mutableListOf("user.id", "user.name"), "kbh")
        given(userService.selectUserList(userSearchDto)).willReturn(mutableListOf(userEntity))
        given(userService.selectUser("kbh")).willReturn(userEntity)

    }

    @Test
    @WithMockUser
    fun 일반_컨트롤러_맵핑_결과() {
        mockMvc.perform(get("/users/search")).andExpect(status().isOk)

        mockMvc.perform(
            get("/users/list")
                .param("searchKey", "user.id", "user.name")
                .param("searchValue", "kbh")
        )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())

        mockMvc.perform(get("/users/{userId}/edit", "kbh")).andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())

    }

    @Test
    @WithMockUser
    fun REST_사용자_전체조회() {
        mockMvc.perform(
            get("/rest/users")
                .param("searchKey", "user.id", "user.name")
                .param("searchValue", "kbh")
        ).andExpect(status().isOk)
    }

    @Test
    @WithMockUser
    fun REST_사용자_단일조회() {
        mockMvc.perform(get("/rest/users/{userId}", "kbh")).andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @WithMockUser
    fun REST_사용자_업데이트() {
        mockMvc.perform(
            put("/rest/users/{userId}", "kbh")
                .param("userId", "kbh")
                .with(csrf())
        ).andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

}
