package co.brainz.workflow

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.numbering.entity.AliceNumberingPatternEntity
import co.brainz.framework.numbering.entity.AliceNumberingRuleEntity
import co.brainz.framework.numbering.repository.AliceNumberingPatternRepository
import co.brainz.framework.numbering.repository.AliceNumberingRuleRepository
import co.brainz.workflow.component.entity.WfComponentDataEntity
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.component.repository.WfComponentDataRepository
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.document.entity.WfDocumentDisplayEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementDataRepository
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.form.repository.WfFormRepository
import co.brainz.workflow.process.entity.WfProcessEntity
import co.brainz.workflow.process.repository.WfProcessRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.time.LocalDateTime
import java.util.UUID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Configuration {

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
    lateinit var wfElementDataRepository: WfElementDataRepository

    @Autowired
    lateinit var wfDocumentRepository: WfDocumentRepository

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    private val jsonFilePath = "src/test/kotlin/co/brainz/workflow/json"
    private val userJsonFile = "users.json"
    private val numberingJsonFile = "numberings.json"
    private val formJsonFile = "forms.json"
    private val processJsonFile = "processes.json"
    private val documentJsonFile = "documents.json"

    private var data = DataDto()

    fun getData(): DataDto {
        return this.data
    }

    /**
     * Set users.
     */
    fun setUsers(customDataList: MutableList<AliceUserEntity>?) {
        var users: MutableList<AliceUserEntity> = mutableListOf()
        when (customDataList) {
            null -> {
                val jsonFile = File(jsonFilePath + File.separator + userJsonFile)
                if (jsonFile.exists()) {
                    val jsonNode = mapper.readTree(jsonFile.readText(Charsets.UTF_8))
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
                        aliceUserRepository.saveAll(users)
                    }
                }
            }
            else -> users = aliceUserRepository.saveAll(customDataList)
        }
        this.data.users = users
    }

    /**
     * Set numbering.
     */
    fun setNumbering(customDataList: MutableList<AliceNumberingRuleEntity>?) {
        var numberings: MutableList<AliceNumberingRuleEntity> = mutableListOf()
        when (customDataList) {
            null -> {
                val jsonFile = File(jsonFilePath + File.separator + numberingJsonFile)
                if (jsonFile.exists()) {
                    val jsonNode = mapper.readTree(jsonFile.readText(Charsets.UTF_8))
                    jsonNode.forEach { numbering ->
                        var numberingRuleEntity = AliceNumberingRuleEntity(
                            numberingId = numbering["id"].asText(),
                            numberingName = numbering["name"].asText()
                        )
                        numberingRuleEntity = wfAliceNumberingRuleRepository.save(numberingRuleEntity)
                        val numberingPatternEntities: MutableList<AliceNumberingPatternEntity> = mutableListOf()
                        numbering["patterns"].forEach { pattern ->
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
                        numberingRuleEntity.patterns = wfAliceNumberingPatternRepository.saveAll(numberingPatternEntities)
                        numberings.add(numberingRuleEntity)
                    }
                    wfAliceNumberingRuleRepository.saveAll(numberings)
                }
            }
            else -> numberings = wfAliceNumberingRuleRepository.saveAll(customDataList)
        }
        this.data.numberingRule = numberings
    }

    /**
     * Set forms.
     */
    fun setForms(customDataList: MutableList<WfFormEntity>?) {
        var forms: MutableList<WfFormEntity> = mutableListOf()
        when (customDataList) {
            null -> {
                val jsonFile = File(jsonFilePath + File.separator + formJsonFile)
                if (jsonFile.exists()) {
                    val jsonNode = mapper.readTree(jsonFile.readText(Charsets.UTF_8))
                    jsonNode.forEach { form ->
                        var formEntity = WfFormEntity(
                            formId = "",
                            formName = form["name"].asText(),
                            formStatus = form["status"].asText(),
                            createDt = LocalDateTime.now(),
                            createUser = aliceUserRepository.findByUserId("admin")
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
                    wfFormRepository.saveAll(forms)
                }
            }
            else -> forms = wfFormRepository.saveAll(customDataList)
        }
        this.data.forms = forms
    }

    /**
     * Set processes.
     */
    fun setProcesses(customDataList: MutableList<WfProcessEntity>?) {
        var processes: MutableList<WfProcessEntity> = mutableListOf()
        when (customDataList) {
            null -> {
                val jsonFile = File(jsonFilePath + File.separator + processJsonFile)
                if (jsonFile.exists()) {
                    val jsonNode = mapper.readTree(jsonFile.readText(Charsets.UTF_8))
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
                                    attributeValue = aliceUserRepository.findByUserId("admin").userKey
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
                            elementEntity.elementDataEntities.addAll(wfElementDataRepository.saveAll(elementDataEntities))
                            elementEntities.add(elementEntity)
                        }
                        processEntity.elementEntities = elementEntities
                        processes.add(processEntity)
                    }
                    wfProcessRepository.saveAll(processes)
                }
            }
            else -> processes = wfProcessRepository.saveAll(customDataList)
        }
        this.data.processes = processes
    }

    /**
     * Set documents.
     */
    fun setDocument(customDataList: MutableList<WfDocumentEntity>?, process: WfProcessEntity?, form: WfFormEntity?, numbering: AliceNumberingRuleEntity?) {
        var documents: MutableList<WfDocumentEntity> = mutableListOf()
        when (customDataList) {
            null -> {
                val jsonFile = File(jsonFilePath + File.separator + documentJsonFile)
                if (jsonFile.exists()) {
                    val jsonNode = mapper.readTree(jsonFile.readText(Charsets.UTF_8))
                    var processEntity = WfProcessEntity()
                    var formEntity = WfFormEntity()
                    var numberingEntity = AliceNumberingRuleEntity(
                        numberingId = "",
                        numberingName = ""
                    )
                    if (process == null) {
                        processEntity = this.data.processes!![0]
                    }
                    if (form == null) {
                        formEntity = this.data.forms!![0]
                    }
                    if (numbering == null) {
                        numberingEntity = this.data.numberingRule!![0]
                    }
                    jsonNode.forEach { document ->
                        var documentEntity = WfDocumentEntity(
                            documentId = "",
                            documentName = document["name"].asText(),
                            documentType = document["type"].asText(),
                            documentStatus = document["status"].asText(),
                            documentColor = document["color"].asText(),
                            documentGroup = document["group"].asText(),
                            form = formEntity,
                            process = processEntity,
                            numberingRule = numberingEntity
                        )
                        documentEntity = wfDocumentRepository.save(documentEntity)
                        documents.add(documentEntity)
                        // document display?
                    }
                }
                wfDocumentRepository.saveAll(documents)
            } else -> documents = wfDocumentRepository.saveAll(customDataList)
        }
        this.data.documents = documents
    }
}
