/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.service

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.itsm.statistic.customChart.dto.ChartRange
import co.brainz.itsm.statistic.customChart.dto.average.ChartTokenData
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.form.repository.WfFormRepository
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import org.springframework.stereotype.Service
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.itsm.statistic.customChart.dto.ChartTagInstanceDto
import co.brainz.workflow.document.repository.WfDocumentRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

@Service
class ChartManagerService(
    private val aliceTagRepository: AliceTagRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val wfFormRepository: WfFormRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfDocumentRepository: WfDocumentRepository,
    private val aliceUserRepository: AliceUserRepository
) {
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getTagValueList(tagType: String, tagValue: List<String>): List<AliceTagDto> {
        return aliceTagRepository.findByTagValueIn(tagType, tagValue)
    }

    fun getComponentList(tagTargetIds: List<String>): List<WfComponentEntity> {
        return wfComponentRepository.findByComponentIdIn(tagTargetIds)
    }

    fun getFormList(formIds: List<String>): List<WfFormEntity> {
        return wfFormRepository.findByFormIdIn(formIds)
    }

    fun getInstanceListInTag(tagValue: String, range: ChartRange, documentStatus: String?): List<WfInstanceEntity> {
        return wfInstanceRepository.getInstanceListInTag(tagValue, range, documentStatus)
    }

    fun getLastTokenList(instanceIds: Set<String>): List<WfTokenEntity> {
        return wfTokenRepository.getLastTokenList(instanceIds)
    }

    fun getTokenDataList(componentIds: Set<String>, tokenIds: Set<String>, componentTypeSet: Set<String>): List<ChartTokenData> {
        return wfTokenDataRepository.getTokenDataList(componentIds, tokenIds, componentTypeSet)
    }

    //더미데이터로 차트확인 
    fun getFileReadFun(): List<ChartTagInstanceDto> {
        val findDocument = wfDocumentRepository.findDocumentEntityByDocumentId("4028b21f7c90d996017c91ae7987004f")
        val user = aliceUserRepository.findAliceUserEntityByUserKey("0509e09412534a6e98f04ca79abb6424")
        val chartList : MutableList<ChartTagInstanceDto> = mutableListOf()

        val file = File("src/main/kotlin/co/brainz/itsm/statistic/customChart/instances.json")

        val params = mapper.readValue(file, Map::class.java)
        val paramList = params["ChartTagInstanceDto"]
        val list: List<Map<String, Any>> = paramList as List<Map<String,Any>>

        for(param in list) {
            val instanceList: MutableList<WfInstanceEntity> = mutableListOf()
            val tag = param["tag"].toString()
            val parser = JsonParser()
            val element: JsonElement = parser.parse(tag)
            val tagId: String = element.getAsJsonObject().get("tagId").getAsString()
            val tagType: String = element.getAsJsonObject().get("tagType").getAsString()
            val tagValue: String = element.getAsJsonObject().get("tagValue").getAsString()
            val targetId: String = element.getAsJsonObject().get("targetId").getAsString()
            val aliceTagDto = AliceTagDto(
                tagId = tagId,
                tagType = tagType,
                tagValue = tagValue,
                targetId = targetId
            )

            val instances:ArrayList<LinkedHashMap<String,Any>> = param.get("instances") as ArrayList<LinkedHashMap<String,Any>>

            for(instance in instances) {
                val instanceId = instance.get("instanceId")
                val instanceStatus = instance.get("instanceStatus")
                val instanceStartDt = instance.get("instanceStartDt")
                val instanceEndDt = instance.get("instanceEndDt")
                val instanceCreateUser = user
                val pTokenId = instance.get("pTokenId")
                val document = findDocument
                val documentNo = instance.get("documentNo")
                val instancePlatform = instance.get("instancePlatform")
                val patten: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.US)
                val formattedStartDt: LocalDateTime? = instanceStartDt?.let { LocalDateTime.parse(it.toString(), patten) }
                val formattedEndDt: LocalDateTime? = instanceEndDt?.let { LocalDateTime.parse(it.toString(), patten) }

                val wfInstanceEntity = WfInstanceEntity(
                    instanceId = instanceId.toString(),
                    instanceStatus = instanceStatus.toString(),
                    instanceStartDt = formattedStartDt as LocalDateTime,
                    instanceEndDt = formattedEndDt as LocalDateTime,
                    instanceCreateUser = instanceCreateUser,
                    document = document,
                    pTokenId = pTokenId as String?,
                    documentNo = documentNo as String?,
                    instancePlatform = instancePlatform as String?
                )
                instanceList.add(wfInstanceEntity)

            }
            chartList.add(
                ChartTagInstanceDto(
                    tag = aliceTagDto,
                    instances = instanceList

                )
            )
        }

        return chartList
    }

}
