/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.engine.manager

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.itsm.numberingRule.entity.NumberingRuleEntity
import co.brainz.itsm.numberingRule.service.NumberingRuleService
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementDataRepository
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.process.entity.WfProcessEntity
import co.brainz.workflow.process.repository.WfProcessRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.time.LocalDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class InitTestData {

    @Autowired
    lateinit var aliceUserRepository: AliceUserRepository

    @Autowired
    lateinit var wfProcessRepository: WfProcessRepository

    @Autowired
    lateinit var wfElementRepository: WfElementRepository

    @Autowired
    lateinit var wfElementDataRepository: WfElementDataRepository

    @Autowired
    lateinit var wfDocumentRepository: WfDocumentRepository

    @Autowired
    lateinit var wfInstanceRepository: WfInstanceRepository

    @Autowired
    lateinit var numberingRuleService: NumberingRuleService

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    private val jsonFilePath = "src/test/kotlin/co/brainz/workflow/json"
    private val userJsonFile = "users.json"
    private val numberingJsonFile = "numberings.json"
    private val formJsonFile = "forms.json"
    private val processJsonFile = "processes.json"
    private val documentJsonFile = "documents.json"
    private val instanceJsonFile = "instance.json"

    private var initData = InitDataDto()

    fun getData(): InitDataDto {
        return this.initData
    }

    /**
     * ?????? ?????? ([userJsonFile] ????????? ????????? ??????).
     */
    fun setUsers() {
        this.makeUsers(null)
    }

    /**
     * ????????? ?????? ??????.
     */
    fun setUsers(customDataList: MutableList<AliceUserEntity>?) {
        this.makeUsers(customDataList)
    }

    /**
     * ??? ?????? ([formJsonFile] ????????? ??? ??????).
     */
    // fun setForms() {
    //     this.makeForms(null)
    // }

    /**
     * ????????? ??? ??????.
     */
    // fun setForms(customDataList: MutableList<WfFormEntity>?) {
    //     this.makeForms(customDataList)
    // }

    /**
     * ???????????? ?????? ([processJsonFile] ????????? ???????????? ??????).
     */
    fun setProcesses() {
        this.makeProcesses(null, null)
    }

    /**
     * ????????? ???????????? ??????.
     */
    fun setProcesses(customDataList: MutableList<WfProcessEntity>?, user: AliceUserEntity?) {
        this.makeProcesses(customDataList, user)
    }

    /**
     * ?????? ?????? ([documentJsonFile] ????????? ?????? ??????).
     */
    fun setDocuments() {
        this.makeDocuments(null, null, null, null, null)
    }

    /**
     * ????????? ?????? ??????.
     */
    fun setDocuments(
        customDataList: MutableList<WfDocumentEntity>?,
        process: WfProcessEntity?,
        form: WfFormEntity?,
        numbering: NumberingRuleEntity?,
        user: AliceUserEntity?
    ) {
        this.makeDocuments(customDataList, process, form, numbering, user)
    }

    /**
     * Instance ?????? ([instanceJsonFile] ????????? Instance).
     */
    fun setInstance() {
        this.makeInstance(null, null, null, null)
    }

    /**
     * ????????? Instance ??????.
     */
    fun setInstance(
        customData: WfInstanceEntity?,
        document: WfDocumentEntity?,
        user: AliceUserEntity?,
        numbering: NumberingRuleEntity?
    ) {
        this.makeInstance(customData, document, user, numbering)
    }

    /**
     * ????????? ?????? (Multiple).
     */
    private fun makeUsers(customDataList: MutableList<AliceUserEntity>?) {
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
                        users = aliceUserRepository.saveAll(users)
                    }
                }
            }
            else -> users = aliceUserRepository.saveAll(customDataList)
        }
        this.initData.users = users
    }

    /**
     * ??? ????????? ?????? (Multiple).
     */
    // private fun makeForms(customDataList: MutableList<WfFormEntity>?) {
    //     var forms: MutableList<WfFormEntity> = mutableListOf()
    //     when (customDataList) {
    //         null -> {
    //             val jsonFile = File(jsonFilePath + File.separator + formJsonFile)
    //             if (jsonFile.exists()) {
    //                 val jsonNode = mapper.readTree(jsonFile.readText(Charsets.UTF_8))
    //                 jsonNode.forEach { form ->
    //                     var formEntity = WfFormEntity(
    //                         formId = "",
    //                         formName = form["name"].asText(),
    //                         formStatus = form["status"].asText(),
    //                         createDt = LocalDateTime.now(),
    //                         createUser = aliceUserRepository.findByUserId("admin")
    //                     )
    //                     formEntity = wfFormRepository.save(formEntity)
    //
    //                     // component
    //                     val components: MutableList<WfComponentEntity> = mutableListOf()
    //                     form["components"].forEach { component ->
    //                         var componentEntity = WfComponentEntity(
    //                             componentId = UUID.randomUUID().toString().replace("-", ""),
    //                             componentType = component["type"].asText(),
    //                             form = formEntity,
    //                             isTopic = component["isTopic"].asBoolean(),
    //                             mappingId = component["mappingId"].asText()
    //                         )
    //                         componentEntity = wfComponentRepository.save(componentEntity)
    //
    //                         // component data
    //                         val componentDataEntities: MutableList<WfComponentDataEntity> = mutableListOf()
    //                         component["attributes"].forEach { componentData ->
    //                             val componentDatEntity = WfComponentDataEntity(
    //                                 componentId = componentEntity.componentId,
    //                                 attributeId = componentData["id"].asText(),
    //                                 attributes = componentEntity,
    //                                 attributeValue = componentData["value"].asText()
    //                             )
    //                             componentDataEntities.add(componentDatEntity)
    //                         }
    //                         componentEntity.attributes!!.addAll(
    //                             wfComponentDataRepository.saveAll(componentDataEntities)
    //                         )
    //                         components.add(componentEntity)
    //                     }
    //                     formEntity.components.addAll(components)
    //                     forms.add(formEntity)
    //                 }
    //                 wfFormRepository.saveAll(forms)
    //             }
    //         }
    //         else -> forms = wfFormRepository.saveAll(customDataList)
    //     }
    //     this.initData.forms = forms
    // }

    /**
     * ???????????? ?????? (Multiple).
     */
    private fun makeProcesses(customDataList: MutableList<WfProcessEntity>?, user: AliceUserEntity?) {
        var processes: MutableList<WfProcessEntity> = mutableListOf()
        when (customDataList) {
            null -> {
                val jsonFile = File(jsonFilePath + File.separator + processJsonFile)
                if (jsonFile.exists()) {
                    val jsonNode = mapper.readTree(jsonFile.readText(Charsets.UTF_8))
                    val userEntity = user ?: this.initData.users!![0]
                    jsonNode.forEach { process ->
                        var processEntity = WfProcessEntity(
                            processId = "",
                            processName = process["name"].asText(),
                            processStatus = process["status"].asText(),
                            createDt = LocalDateTime.now(),
                            createUser = userEntity
                        )
                        processEntity = wfProcessRepository.save(processEntity)

                        val elementEntities: MutableList<WfElementEntity> = mutableListOf()
                        process["elementEntities"].forEach { element ->
                            var elementEntity = WfElementEntity(
                                // elementId = UUID.randomUUID().toString().replace("-", ""),
                                elementId = element["id"].asText(),
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
                                if (data["id"].asText() == "id") {
                                    attributeValue = elementEntity.elementId
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
                            elementEntity.elementDataEntities.addAll(
                                wfElementDataRepository.saveAll(elementDataEntities)
                            )
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
        this.initData.processes = processes
    }

    /**
     * ?????? ?????? (Multiple).
     */
    private fun makeDocuments(
        customDataList: MutableList<WfDocumentEntity>?,
        process: WfProcessEntity?,
        form: WfFormEntity?,
        numbering: NumberingRuleEntity?,
        user: AliceUserEntity?
    ) {
        var documents: MutableList<WfDocumentEntity> = mutableListOf()
        when (customDataList) {
            null -> {
                val jsonFile = File(jsonFilePath + File.separator + documentJsonFile)
                if (jsonFile.exists()) {
                    val jsonNode = mapper.readTree(jsonFile.readText(Charsets.UTF_8))
                    val processEntity = process ?: this.initData.processes!![0]
                    val formEntity = form ?: this.initData.forms!![0]
                    val numberingRuleEntity = numbering ?: this.initData.numberingRule!![0]
                    val userEntity = user ?: this.initData.users!![0]
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
                            numberingRule = numberingRuleEntity,
                            createDt = LocalDateTime.now(),
                            createUserKey = userEntity.userKey
                        )
                        documentEntity = wfDocumentRepository.save(documentEntity)
                        documents.add(documentEntity)
                        // document display?
                    }
                }
                wfDocumentRepository.saveAll(documents)
            }
            else -> documents = wfDocumentRepository.saveAll(customDataList)
        }
        this.initData.documents = documents
    }

    /**
     * Instance ??????.
     */
    private fun makeInstance(
        customData: WfInstanceEntity?,
        document: WfDocumentEntity?,
        user: AliceUserEntity?,
        numbering: NumberingRuleEntity?
    ) {
        when (customData) {
            null -> {
                val jsonFile = File(jsonFilePath + File.separator + instanceJsonFile)
                if (jsonFile.exists()) {
                    val jsonNode = mapper.readTree(jsonFile.readText(Charsets.UTF_8))
                    val userEntity = user ?: this.initData.users!![0]
                    val numberingEntity = numbering ?: this.initData.numberingRule!![0]
                    val instance = WfInstanceEntity(
                        instanceId = "",
                        document = document ?: this.initData.documents!![0],
                        instanceStatus = jsonNode["status"].asText(),
                        instanceStartDt = LocalDateTime.now(),
                        instanceCreateUser = userEntity,
                        pTokenId = jsonNode["pTokenId"].asText(),
                        documentNo = numberingRuleService.getNewNumbering(numberingEntity.numberingId)
                    )
                    this.initData.instance = wfInstanceRepository.save(instance)
                }
            }
            else -> this.initData.instance = wfInstanceRepository.save(customData)
        }
    }
}
