package co.brainz.itsm.user

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
class UserTest {

    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    @Before
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    fun testUserView() {
        mockMvc.perform(get("/users/search"))
                .andExpect(status().isOk)

    }

    @Test
    fun testGetUserList() {
        mockMvc.perform(get("/users/list")
                    .param("searchKey", "user.id", "user.name")
                    .param("searchValue", "kbh"))
                .andExpect(status().isOk)
                .andDo(MockMvcResultHandlers.print())

    }
}