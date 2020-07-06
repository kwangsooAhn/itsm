package co.brainz.workflow

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.numbering.constants.AliceNumberingConstants
import co.brainz.framework.numbering.entity.AliceNumberingPatternEntity
import co.brainz.framework.numbering.entity.AliceNumberingRuleEntity
import co.brainz.framework.numbering.repository.AliceNumberingPatternRepository
import co.brainz.framework.numbering.repository.AliceNumberingRuleRepository
import co.brainz.workflow.component.entity.WfComponentDataEntity
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.component.repository.WfComponentDataRepository
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementDataRepository
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.form.repository.WfFormRepository
import co.brainz.workflow.process.entity.WfProcessEntity
import co.brainz.workflow.process.repository.WfProcessRepository
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import java.io.File
import javax.transaction.Transactional
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import java.time.LocalDateTime
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class UserTask {


    @Autowired
    lateinit var mockMvc: MockMvc

    //@Autowired
    //lateinit var wfInstanceService: WfInstanceService

    //@Autowired
    //lateinit var wfTokenManagerService: WfTokenManagerService

    @Autowired
    lateinit var aliceUserRepository: AliceUserRepository

    @Autowired
    lateinit var wfAliceNumberingRuleRepository: AliceNumberingRuleRepository

    @Autowired
    lateinit var wfAliceNumberingPatternRepository: AliceNumberingPatternRepository

    @Autowired
    lateinit var wfFormRepository: WfFormRepository

    @Autowired
    lateinit var wfComponentRepository: WfComponentRepository

    @Autowired
    lateinit var wfComponentDataRepository: WfComponentDataRepository

    @Autowired
    lateinit var wfProcessRepository: WfProcessRepository

    @Autowired
    lateinit var wfElementRepository: WfElementRepository

    @Autowired
    lateinit var wfElementDatRepository: WfElementDataRepository

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    private var data = DataDto()


    @Test
    fun init() {
        val configureFile = File("src/test/kotlin/co/brainz/workflow/configurationData2json")
        if (configureFile.exists()) {
            val configValue = mapper.readTree(configureFile.readText(Charsets.UTF_8))

            // Setting
            this.setUser(configValue["users"]!!)
            this.setNumberingRule(configValue["numbering"]!!)
            this.setForm(configValue["forms"]!!)
            //this.setProcess(configValue["processes"]!!)

            // Validation
            assertThat(data.users).isNotNull
            assertThat(data.numberingRule).isNotNull
            assertThat(data.forms).isNotNull
            //assertThat(data.processes).isNotNull

            println(aliceUserRepository.findByUserId("junit"))
            println(wfAliceNumberingRuleRepository.findAll())
            println(wfFormRepository.findWfFormEntityByFormId(data.forms?.get(0)?.formId!!))

            /*println("-----------")
            println(wfElementRepository.findAll())
            println("-----------")
            println(wfElementDatRepository.findAll())
            println("-----------")
            println(wfProcessRepository.findAll())
            println("-----------")
            println(wfFormRepository.findAll())
            println("-----------")*/

        }


    }

    private fun setProcess(jsonNode: JsonNode) {
        var processes: MutableList<WfProcessEntity> = mutableListOf()
        val assignee = data.users?.get(0)?.userKey!!

        jsonNode.forEach { process ->
            var processEntity = WfProcessEntity(
                processId = "",
                processName = process["name"].asText(),
                processStatus = process["status"].asText()
            )
            processEntity = wfProcessRepository.save(processEntity)

            val elementEntities: MutableList<WfElementEntity> = mutableListOf()
            process["elementEntities"].forEach { element ->
                var elementEntity = WfElementEntity(
                    elementId = UUID.randomUUID().toString().replace("-", ""),
                    processId = processEntity.processId,
                    elementName = element["name"].asText(),
                    elementType = element["type"].asText(),
                    notification = element["notification"].asBoolean()
                )
                elementEntity = wfElementRepository.save(elementEntity)

                val elementDataEntities: MutableList<WfElementDataEntity> = mutableListOf()
                element["data"].forEach { data ->
                    var attributeValue = data["value"].asText()
                    if (data["id"].asText() == "assignee") {
                        attributeValue = assignee
                    }
                    val elementDataEntity = WfElementDataEntity(
                        element = elementEntity,
                        attributeId = data["id"].asText(),
                        attributeValue = attributeValue,
                        attributeRequired = data["required"].asBoolean(),
                        attributeOrder = data["order"].asInt()
                    )
                    elementDataEntities.add(elementDataEntity)
                }
                elementEntity.elementDataEntities.addAll(wfElementDatRepository.saveAll(elementDataEntities))
                elementEntities.add(elementEntity)
            }
            processEntity.elementEntities = elementEntities
            processes.add(processEntity)
        }
        if (processes.isNotEmpty()) {
            processes = wfProcessRepository.saveAll(processes)
            this.data.processes = processes
        }
    }

    private fun setForm(jsonNode: JsonNode) {
        var forms: MutableList<WfFormEntity> = mutableListOf()

        jsonNode.forEach { form ->
            var formEntity = WfFormEntity(
                formId = "",
                formName = form["name"].asText(),
                formStatus = form["status"].asText(),
                createDt = LocalDateTime.now(),
                createUser = data.users?.get(0)
            )
            formEntity = wfFormRepository.save(formEntity)

            //component
            val components: MutableList<WfComponentEntity> = mutableListOf()
            form["components"].forEach { component ->
                var componentEntity = WfComponentEntity(
                    componentId = UUID.randomUUID().toString().replace("-", ""),
                    componentType = component["type"].asText(),
                    form = formEntity,
                    isTopic = component["isTopic"].asBoolean(),
                    mappingId = component["mappingId"].asText()
                )
                componentEntity = wfComponentRepository.save(componentEntity)

                //component data
                val componentDataEntities: MutableList<WfComponentDataEntity> = mutableListOf()
                component["attributes"].forEach { componentData ->
                    val componentDatEntity = WfComponentDataEntity(
                        componentId = componentEntity.componentId,
                        attributeId = componentData["id"].asText(),
                        attributes = componentEntity,
                        attributeValue = componentData["value"].asText()
                    )
                    componentDataEntities.add(componentDatEntity)
                }
                componentEntity.attributes!!.addAll(wfComponentDataRepository.saveAll(componentDataEntities))
                components.add(componentEntity)
            }
            formEntity.components!!.addAll(components)
            forms.add(formEntity)
        }

        if (forms.isNotEmpty()) {
            forms = wfFormRepository.saveAll(forms)
            this.data.forms = forms
        }
    }

    private fun setUser(jsonNode: JsonNode) {
        var users: MutableList<AliceUserEntity> = mutableListOf()
        jsonNode.forEach { user ->
            users.add(
                AliceUserEntity(
                    userKey = user["key"].asText(),
                    userId = user["id"].asText(),
                    userName = user["name"].asText(),
                    status = user["status"].asText(),
                    useYn = user["useYn"].asBoolean(),
                    lang = user["lang"].asText(),
                    timeFormat = user["timeFormat"].asText(),
                    platform = user["platform"].asText(),
                    timezone = user["timezone"].asText()
                )
            )
        }
        if (users.isNotEmpty()) {
            users = aliceUserRepository.saveAll(users)
            this.data.users = users
        }
    }

    private fun setNumberingRule(jsonNode: JsonNode) {
        var numberingRuleEntity = AliceNumberingRuleEntity(
            numberingId = jsonNode["id"].asText(),
            numberingName = jsonNode["name"].asText()
        )
        numberingRuleEntity = wfAliceNumberingRuleRepository.save(numberingRuleEntity)

        val numberingPatternJsonNode = jsonNode["patterns"]
        val numberingPatternEntities: MutableList<AliceNumberingPatternEntity> = mutableListOf()
        numberingPatternJsonNode.forEach { pattern ->
            numberingPatternEntities.add(
                AliceNumberingPatternEntity(
                    patternId = pattern["id"].asText(),
                    numberingRule = numberingRuleEntity,
                    patternName = pattern["name"].asText(),
                    patternType = pattern["type"].asText(),
                    patternValue = pattern["value"].asText(),
                    patternOrder = pattern["order"].asInt()
                )
            )
        }
        if (numberingPatternEntities.isNotEmpty()) {
            numberingRuleEntity.patterns = wfAliceNumberingPatternRepository.saveAll(numberingPatternEntities)
        }

        if (numberingRuleEntity.numberingId.isNotEmpty()) {
           // this.data.numberingRule = numberingRuleEntity
        }
    }

/*
    lateinit var tokenDto: WfTokenDto

    // 초기 데이터를 저장한다.
    //@Test
    fun initData() {
        // Document
        val documentId = "40288ab27235e157017235f31f430009"
        val result = mockMvc.perform(get("/rest/wf/documents/$documentId")).andReturn()
        val document = mapper.readValue(result.response.contentAsString, WfDocumentEntity::class.java)

    }*/

   /* // Create Instance
    @Test
    fun createInstance() {
        val tokenDto = WfTokenDto(
            documentId = "40288ab27235e157017235f31f430009"
        )
        val instance = wfInstanceService.createInstance(tokenDto)
        tokenDto.instanceId = instance.instanceId
        tokenDto.instanceCreateUser = instance.instanceCreateUser

        createToken(tokenDto)
    }

    fun dss() {
        // 생성된 instance 에 userTask 토큰을 처리하도록 진행

    }


    fun createToken(newTokenDto: WfTokenDto): WfTokenDto {
        this.assigneeId = newTokenDto.assigneeId.toString()
        this.tokenEntity = wfTokenManagerService.saveToken(newTokenDto)
        newTokenDto.tokenId = this.tokenEntity.tokenId

        //this.createElementToken(newTokenDto)
        //wfTokenManagerService.notificationCheck(tokenEntity)
        return newTokenDto
    }



    // 테스트용 데이터 설정 (이게 정리가 되어야 한다.)




    // create token 이후 처리되는 createElementToken 을 호출하여 응답 확인

    //@Test
    fun test() {
        //val test: LinkedHashMap = MultiValueMap<String, String>
        val params = LinkedMultiValueMap<String, String>()
        params["assignee"] = "40288ab26fa3219e016fa32231230000"
        params["tokenStatus"] = "token.status.finish"
        mockMvc.perform(get("/rest/wf/tokens").params(params))
            .andExpect(status().isOk)
            //.andDo(println())

        //server.expect(requestTo("https://localhost/rest/wf/tokens"))
            //.andRespond()
    }*/
}
