package co.brainz.itsm.notice.repsitory

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
import java.time.LocalDateTime
import co.brainz.itsm.notice.repository.NoticeRepository
import co.brainz.itsm.notice.entity.NoticeEntity
import co.brainz.itsm.notice.service.NoticeService

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class NoticeTest() {
    
    @Autowired
    lateinit var noticeRepository: NoticeRepository
    
    @Autowired
    lateinit var noticeService: NoticeService
    
    @Before
    fun before() {
        println("before")
    }

    @Test
    fun save() {
        var inputDate = LocalDateTime.now()
        noticeRepository.save(
            NoticeEntity(
                noticeNo = 'test'
                noticeTitle = "테스트 123",
                noticeContents =  "테스트 내용",
                popYn = true,
                popStrtDt = inputDate,
                popEndDt = inputDate,
                popWidth = 500,
                popHeight = 500,
                topNoticeYn = true,
                topNoticeStrtDt = inputDate,
                topNoticeEndDt = inputDate,
                createUserid = "testUser",
                createDt = inputDate,
                updateUserid = null,
                updateDt = null
            )
        )
    }
    
    @After
    fun after() {
/*        noticeRepository.deleteById('')*/
    } 
}
