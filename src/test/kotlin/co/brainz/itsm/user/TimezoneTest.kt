package co.brainz.itsm.user

/*import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceUrlEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.auth.entity.AliceMenuEntity
import co.brainz.itsm.notice.entity.NoticeEntity
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.context.WebApplicationContext
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.TimeZone

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)*/
class TimezoneTest {
/*

    @Autowired
    lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    lateinit var userDetailsService: AliceUserDetailsService

    lateinit var mockMvc: MockMvc
    lateinit var securityContext: SecurityContext
    lateinit var usernamePasswordAuthenticationToken: UsernamePasswordAuthenticationToken

    val userId = "lizeelf"
    val createDt: LocalDateTime = LocalDateTime.now()

    @Before
    fun init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
        securityContext = SecurityContextHolder.getContext()
    }

    fun authorities(aliceUser: AliceUserEntity): Set<GrantedAuthority> {
        val authorities = mutableSetOf<GrantedAuthority>()
        aliceUser.getAuthorities().forEach {
            authorities.add(SimpleGrantedAuthority(it.authority))
        }
        return authorities
    }

    fun authList(aliceUser: AliceUserEntity): Set<AliceAuthEntity> {
        val authId = mutableSetOf<String>()
        aliceUser.getAuthorities().forEach {
            authId.add(it.authority)
        }
        return userDetailsService.getAuthList(authId)
    }

    fun menuList(authList: Set<AliceAuthEntity>): Set<AliceMenuEntity> {
        val menuList = mutableSetOf<AliceMenuEntity>()
        authList.forEach {auth ->
            auth.menuAuthMapEntities.forEach {menuAuthMap ->
                menuList.add(menuAuthMap.menu)
            }
        }
        return menuList
    }

    fun urlList(authList: Set<AliceAuthEntity>): Set<AliceUrlEntity> {
        val urlList = mutableSetOf<AliceUrlEntity>()
        authList.forEach {auth ->
            auth.urlAuthMapEntities.forEach {urlAuthMap ->
                urlList.add(urlAuthMap.url)
            }
        }
        return urlList
    }

    @Transactional
    fun makeToken(timezone: String) {
        val aliceUser = userDetailsService.loadUserByUsername(userId)
        val authorities = authorities(aliceUser)
        val authList = authList(aliceUser)
        val menuList = menuList(authList)
        val urlList = urlList(authList)
        var userTimezone = aliceUser.timezone
        if (timezone != "") {
            userTimezone = timezone
        }
        usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userId, aliceUser.password, authorities)
        usernamePasswordAuthenticationToken.details = AliceUserDto(
                aliceUser.userKey, aliceUser.userId, aliceUser.userName, aliceUser.email, aliceUser.useYn,
                aliceUser.tryLoginCount, aliceUser.expiredDt, aliceUser.oauthKey, authorities, menuList, urlList, userTimezone, aliceUser.lang, aliceUser.timeFormat
        )
        securityContext.authentication = usernamePasswordAuthenticationToken
    }

    //http://praxisit.de/convert-localdatetime-to-json-with-kotlin/
    fun dataMapper(noticeDto: NoticeEntity): String {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerModule(ParameterNamesModule())
        mapper.registerModule(Jdk8Module())
        mapper.registerModule(KotlinModule())
        val ow = mapper.writer().withDefaultPrettyPrinter()
        return ow.writeValueAsString(noticeDto)
    }

    */
/**
     * 공지사항 등록(기존데이터 삭제후).
     *//*

    @Test
    fun insertData() {
        makeToken("")
        val noticeList: List<NoticeEntity> = getNoticeInfo(createDt)
        for (notice in noticeList) {
            deleteNotice(notice.noticeNo)
        }
        val noticeDto = NoticeEntity(
                noticeTitle = "JUNIT_TEST",
                noticeContents = "JUNIT TEST",
                popYn = false,
                topNoticeYn = false
        )

        val strValue = dataMapper(noticeDto)
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/notices")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(strValue))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    fun deleteNotice(noticeNo: String) {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/notices/${noticeNo}")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    fun setParameters(): MultiValueMap<String, String> {
        val parameters: MultiValueMap<String, String> = LinkedMultiValueMap()
        parameters.add("noticeTitle", "true")
        parameters.add("createUserKey", "false")
        parameters.add("keyWord", "JUNIT_TEST")
        parameters.add("fromDt", createDt.minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        parameters.add("toDt", createDt.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        return parameters
    }

    fun getNoticeInfo(createDt1: LocalDateTime): List<NoticeEntity> {
        var noticeList: List<NoticeEntity> = emptyList()
        val parameters = setParameters();
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/notices/list")
                .params(parameters)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.model().attributeExists("noticeList"))

        val dataList = result.andReturn().modelAndView?.modelMap?.get("noticeList") as List<NoticeEntity>
        if (dataList.isNotEmpty()) {
            noticeList = dataList
        }
        return noticeList
    }

    */
/**
     * 공지사항 조회.
     *//*

    @Test
    fun searchData() {
        val timezone = "Africa/Cairo"
        makeToken(timezone)
        val parameters = setParameters()
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/notices/list")
                .params(parameters)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.model().attributeExists("noticeList"))

        val noticeList = result.andReturn().modelAndView?.modelMap?.get("noticeList") as List<NoticeEntity>
        Assertions.assertThat(noticeList).isNotNull
        val dataDt = noticeList[0].createDt.withNano(0).atZone(ZoneId.of(timezone)).withZoneSameInstant(ZoneId.of(TimeZone.getDefault().id)).toLocalDateTime()
        Assertions.assertThat(noticeList[0].createDt.withNano(0).isEqual(dataDt))
        println(">> Insert Time : $createDt")
        println(">> TimeZone: $timezone")
        println(">> TimeZone Time: ${noticeList[0].createDt.withNano(0)}")
        println(">> Local Time: $dataDt")
    }
*/

}
