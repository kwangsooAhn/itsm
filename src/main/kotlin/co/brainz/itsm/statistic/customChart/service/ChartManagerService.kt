/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.service

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
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

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
    fun getFileReadFun(aliceTagDto: List<AliceTagDto>): List<ChartTagInstanceDto> {
        val findDocument = wfDocumentRepository.findDocumentEntityByDocumentId("4028b21f7c90d996017c91ae7987004f")
        val user = aliceUserRepository.findAliceUserEntityByUserKey("0509e09412534a6e98f04ca79abb6424")
        val chartList: MutableList<ChartTagInstanceDto> = mutableListOf()
        val file = File("src/main/kotlin/co/brainz/itsm/statistic/customChart/instances.json")
        val params = mapper.readValue(file, Map::class.java)
        val paramList = params["ChartTagInstanceDto"]
        val list: List<Map<String, Any>> = paramList as List<Map<String, Any>>

        if (aliceTagDto.size != 0) {
            for (param in list) {
                val instanceList: MutableList<WfInstanceEntity> = mutableListOf()
                val tag: Map<String, Any> = param["tag"] as Map<String, Any>
                val tagId = tag["tagId"]
                var tagData = AliceTagDto()

                when (tagId) {
                    "1" ->
                        tagData = AliceTagDto(
                            tagId = aliceTagDto.component1().tagId,
                            tagValue = aliceTagDto.component1().tagValue,
                            tagType = aliceTagDto.component1().tagType,
                            targetId = aliceTagDto.component1().targetId
                        )
                    "2" ->
                        if (aliceTagDto.size >= 2) {
                            tagData = AliceTagDto(
                                tagId = aliceTagDto.component2().tagId,
                                tagValue = aliceTagDto.component2().tagValue,
                                tagType = aliceTagDto.component2().tagType,
                                targetId = aliceTagDto.component2().targetId
                            )
                        }
                }

                val instances: ArrayList<LinkedHashMap<String, Any>> =
                    param["instances"] as ArrayList<LinkedHashMap<String, Any>>

                if (tagData.tagValue.isNotEmpty()) {
                    for (instance in instances) {
                        val instanceId = instance["instanceId"]
                        val instanceStatus = instance["instanceStatus"]
                        val instanceStartDt = instance["instanceStartDt"]
                        val instanceEndDt = instance["instanceEndDt"]
                        val pTokenId = instance["pTokenId"]
                        val documentNo = instance["documentNo"]
                        val instancePlatform = instance["instancePlatform"]
                        val patten: DateTimeFormatter =
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.US)
                        val formattedStartDt: LocalDateTime? =
                            instanceStartDt?.let { LocalDateTime.parse(it.toString(), patten) }
                        val formattedEndDt: LocalDateTime? =
                            instanceEndDt?.let { LocalDateTime.parse(it.toString(), patten) }

                        val wfInstanceEntity = WfInstanceEntity(
                            instanceId = instanceId.toString(),
                            instanceStatus = instanceStatus.toString(),
                            instanceStartDt = formattedStartDt as LocalDateTime,
                            instanceEndDt = formattedEndDt as LocalDateTime,
                            instanceCreateUser = user,
                            document = findDocument,
                            pTokenId = pTokenId.toString(),
                            documentNo = documentNo.toString(),
                            instancePlatform = instancePlatform.toString()
                        )
                        instanceList.add(wfInstanceEntity)
                    }
                }
                chartList.add(
                    ChartTagInstanceDto(
                        tag = tagData,
                        instances = instanceList
                    )
                )
            }
        }
        return chartList
    }
}
