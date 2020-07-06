package co.brainz.workflow

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.numbering.repository.AliceNumberingRuleRepository
import co.brainz.workflow.form.repository.WfFormRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.transaction.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class Test {

    @Autowired
    lateinit var configuration: Configuration

    @Autowired
    lateinit var aliceUserRepository: AliceUserRepository

    @Autowired
    lateinit var wfAliceNumberingRuleRepository: AliceNumberingRuleRepository

    @Autowired
    lateinit var wfFormRepository: WfFormRepository



    @Test
    fun init() {
        configuration.setUsers(null)
        configuration.setNumbering(null)
        configuration.setForms(null)
        configuration.setProcesses(null)
        configuration.setDocument(null, null, null, null)
        //configuration.getData().processes!![0].elementEntities.forEach { data -> println(data) }
        println("======")
        println(configuration.getData().documents)
    }
}
