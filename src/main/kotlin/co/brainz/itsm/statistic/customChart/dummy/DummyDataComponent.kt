/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */
package co.brainz.itsm.statistic.customChart.dummy

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.itsm.statistic.customChart.dto.ChartTagInstanceDto
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.instance.entity.WfInstanceEntity
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import org.springframework.stereotype.Component

@Component
class DummyDataComponent(
    private val wfDocumentRepository: WfDocumentRepository,
    private val aliceUserRepository: AliceUserRepository
) {
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    // 더미데이터로 차트확인
    fun getDummyDataList(aliceTagDto: List<AliceTagDto>): List<ChartTagInstanceDto> {
        val findDocument = wfDocumentRepository.findDocumentEntityByDocumentId("4028b21f7c90d996017c91ae7987004f")
        val user = aliceUserRepository.findAliceUserEntityByUserKey("0509e09412534a6e98f04ca79abb6424")
        val chartList: MutableList<ChartTagInstanceDto> = mutableListOf()
        val file = File("src/main/kotlin/co/brainz/itsm/statistic/customChart/dummy/DummyInstances.json")
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
