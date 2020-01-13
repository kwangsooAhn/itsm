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
    
    lateinit var savedEntity : NoticeEntity
    
    @Before
    fun save() {
        var inputDate = LocalDateTime.now()
        var entity = NoticeEntity(
            noticeTitle = "Demo Data",
            noticeContents =  "Demo Data",
            popYn = true,
            popStrtDt = inputDate,
            popEndDt = inputDate,
            popWidth = 500,
            popHeight = 500,
            topNoticeYn = true,
            topNoticeStrtDt = inputDate,
            topNoticeEndDt = inputDate,
            createUserkey = "Demo Data",
            createDt = inputDate,
            updateUserkey = null,
            updateDt = null
        )
       
        savedEntity = noticeRepository.save(entity)
      
    }
    
    @Test
    fun findNoticeByNoticeNo() {
        val id  = savedEntity.noticeNo
        val data =  noticeService.findNoticeByNoticeNo(id)
        Assert.assertNotNull(data)
      
    }
    
    @After
    fun deleteById() {
        val id  = savedEntity.noticeNo
        noticeRepository.deleteById(id)
    } 
}
