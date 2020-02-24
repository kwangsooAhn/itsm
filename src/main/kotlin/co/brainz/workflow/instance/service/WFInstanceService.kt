package co.brainz.workflow.instance.service

import co.brainz.workflow.instance.constants.InstanceConstants
import co.brainz.workflow.instance.dto.InstanceDto
import co.brainz.workflow.instance.entity.InstanceMstEntity
import co.brainz.workflow.instance.repository.InstanceMstRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.Gson
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WFInstanceService(private val instanceMstRepository: InstanceMstRepository) {

    /**
     * Instance Create.
     *
     * @param instanceDto
     * @return InstanceMstEntity
     */
    fun createInstance(instanceDto: InstanceDto): InstanceMstEntity {
        val instanceMstEntity = InstanceMstEntity(
                instId = "",
                instStatus = instanceDto.instStatus?:InstanceConstants.Status.RUNNING.code,
                procId = instanceDto.procId,
                instStartDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        return instanceMstRepository.save(instanceMstEntity)
    }

    /**
     * Instance Complete.
     *
     * @param instanceDto
     */
    fun completeInstance(instanceDto: InstanceDto) {
        val instanceMstEntity = instanceMstRepository.findInstanceMstEntityByInstId(instanceDto.instId)
        if (instanceMstEntity.isPresent) {
            instanceMstEntity.get().instStatus = InstanceConstants.Status.FINISH.code
            instanceMstEntity.get().instEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            instanceMstRepository.save(instanceMstEntity.get())
        }

    }

    fun executeInstance(jsonStr: String) {

        println(">>>")
        println(jsonStr)

        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val result: MutableMap<*, *>? = mapper.readValue(jsonStr, MutableMap::class.java)

        //각 DTO에 데이터를 분리하여 넣기
        //넣은 데이터를 이용하여 실행
        val instanceDto = mapper.convertValue(result?.get("instance"), InstanceDto::class.java)
        println(">>>>>>>>>Xxxxx")
        println(instanceDto)
        println("xxxxxx")



    }

}
