package co.brainz.workflow.token.service

import co.brainz.workflow.instance.dto.InstanceDto
import co.brainz.workflow.instance.service.WFInstanceService
import co.brainz.workflow.token.constants.TokenConstants
import co.brainz.workflow.token.dto.TokenDto
import co.brainz.workflow.token.dto.TokenInstanceDto
import co.brainz.workflow.token.dto.TokenProcessDto
import co.brainz.workflow.token.dto.TokenSaveDto
import co.brainz.workflow.token.entity.TokenDataEntity
import co.brainz.workflow.token.entity.TokenMstEntity
import co.brainz.workflow.token.repository.TokenDataRepository
import co.brainz.workflow.token.repository.TokenMstRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WFTokenService(private val tokenMstRepository: TokenMstRepository,
                     private val tokenDataRepository: TokenDataRepository,
                     private val wfInstanceService: WFInstanceService) {

    /**
     * Token Data Converter (String -> Dto).
     *
     * @param jsonValue
     * @return TokenSaveDto
     */
    fun makeTokenData(jsonValue: String): TokenSaveDto {
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val result: MutableMap<*, *>? = mapper.readValue(jsonValue, MutableMap::class.java)
        val tokenInstanceDto = mapper.convertValue(result?.get("instance"), TokenInstanceDto::class.java)
        val tokenProcessDto = mapper.convertValue(result?.get("process"), TokenProcessDto::class.java)
        val tokenDto = mapper.convertValue(result?.get("token"), TokenDto::class.java)
        return TokenSaveDto(
                instanceDto = tokenInstanceDto,
                processDto = tokenProcessDto,
                tokenDto = tokenDto
        )
    }

    /**
     * Token Save.
     *
     * @param jsonValue
     */
    fun postToken(jsonValue: String) {
        val tokenSaveDto = makeTokenData(jsonValue)
        val instanceDto = InstanceDto(instanceId = "", processId = tokenSaveDto.processDto.id)
        val instance = wfInstanceService.createInstance(instanceDto)
        val token = createToken(instance.instId, tokenSaveDto.tokenDto)
        createTokenData(tokenSaveDto, instance.instId, token.tokenId)

        /*val tokenDataEntities: MutableList<TokenDataEntity> = mutableListOf()
        for (tokenDataDto in tokenSaveDto.tokenDto.data!!) {
            val tokenDataEntity = TokenDataEntity(
                    instId = instance.instId,
                    tokenId = token.tokenId,
                    compId = tokenDataDto.compId,
                    value = tokenDataDto.value
            )
            tokenDataEntities.add(tokenDataEntity)
        }*/

        /*if (tokenDataEntities.isNotEmpty()) {
            tokenDataRepository.saveAll(tokenDataEntities)
        }*/

        if (tokenSaveDto.tokenDto.isComplete) {
            tokenComplete()
        }

    }

    fun createTokenData(tokenSaveDto: TokenSaveDto, instId: String, tokenId: String) {
        val tokenDataEntities: MutableList<TokenDataEntity> = mutableListOf()
        for (tokenDataDto in tokenSaveDto.tokenDto.data!!) {
            val tokenDataEntity = TokenDataEntity(
                    instId = instId,
                    tokenId = tokenId,
                    compId = tokenDataDto.compId,
                    value = tokenDataDto.value
            )
            tokenDataEntities.add(tokenDataEntity)
        }
        if (tokenDataEntities.isNotEmpty()) {
            tokenDataRepository.saveAll(tokenDataEntities)
        }
    }

    /**
     * Token Complete.
     */
    fun tokenComplete() {
        //TODO: Token Complete.
    }

    /**
     * Token Update.
     *
     * @param jsonValue
     */
    fun putToken(jsonValue: String) {
        val tokenSaveDto = makeTokenData(jsonValue)
        println(">>>>>>>")
        println(tokenSaveDto)
        println(">>>>>>>")


        deleteTokenData(tokenSaveDto.instanceDto.id, tokenSaveDto.tokenDto.id)

        //token update
        //delete data
        //deleteTokenData(tokenSaveDto.tokenDto)
        //token data insert


    }


    /**
     * Token Create.
     *
     * @param tokenDto
     * @return TokenMstEntity
     */
    fun createToken(instanceId: String, tokenDto: TokenDto): TokenMstEntity {
        val tokenMstEntity = TokenMstEntity(
                tokenId = "",
                instId = instanceId,
                elemId = tokenDto.elemId,
                tokenStatus = TokenConstants.Status.RUNNING.code,
                tokenStartDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        return tokenMstRepository.save(tokenMstEntity)
    }

    fun updateToken() {
        //val tokenMstEntity = tokenMstRepository.findTokenMstEntityByTokenId(tokenDto.tokenId)
    }

/*
    *//**
     * Token Complete.
     *
     * @param tokenDto
     *//*
    fun completeToken(tokenDto: TokenDto) {
        val tokenMstEntity = tokenMstRepository.findTokenMstEntityByTokenId(tokenDto.tokenId)
        if (tokenMstEntity.isPresent) {
            tokenMstEntity.get().tokenStatus = TokenConstants.Status.FINISH.code
            tokenMstEntity.get().tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            tokenMstRepository.save(tokenMstEntity.get())
        }
    }*/

/*    *//**
     * Token Execute(Token + Token Data).
     *
     * @param tokenSaveDto
     *//*
    fun executeToken(tokenSaveDto: TokenSaveDto) {
        when (tokenSaveDto.tokenDto.isComplete) {
            true -> {
                val token =  createToken(tokenSaveDto.tokenDto)
                for (tokenDataDto in tokenSaveDto.tokenDataDtoList) {
                    tokenDataDto.tokenId = token.tokenId
                }
            }
            false -> {
                val tokenMstEntity = tokenMstRepository.findTokenMstEntityByTokenId(tokenSaveDto.tokenDto.tokenId)
                tokenMstEntity.ifPresent {
                    tokenMstEntity.get().tokenStatus = TokenConstants.Status.WAITING.code //sample
                    tokenMstRepository.save(tokenMstEntity.get())
                }
            }
        }
        executeTokenData(tokenSaveDto)
    }

    *//**
     * Token Data Delete + Insert.
     *
     * @param tokenSaveDto
     *//*
    fun executeTokenData(tokenSaveDto: TokenSaveDto) {
        if (!tokenSaveDto.tokenDto.isComplete) {
            deleteTokenData(tokenSaveDto.tokenDto)
        }

        insertTokenData(tokenSaveDto.tokenDataDtoList)
    }

    *//**
     * Token Data Insert.
     *
     * @param tokenDataDtoList
     *//*
    fun insertTokenData(tokenDataDtoList: List<TokenDataDto>) {
        val tokenDataEntities: MutableList<TokenDataEntity> = mutableListOf()
        for (tokenDataDto in tokenDataDtoList) {
            val tokenDataEntity = TokenDataEntity(
                    instId = tokenDataDto.instId,
                    tokenId = tokenDataDto.tokenId,
                    compId = tokenDataDto.compId,
                    value = tokenDataDto.value
            )
            tokenDataEntities.add(tokenDataEntity)
        }
        if (tokenDataEntities.isNotEmpty()) {
            tokenDataRepository.saveAll(tokenDataEntities)
        }
    }
*/
    /**
     * Token Data Delete.
     *
     * @param instanceId
     * @param tokenId
     */
    fun deleteTokenData(instanceId: String, tokenId: String) {
        tokenDataRepository.deleteTokenDataEntityByInstIdAndTokenId(instanceId, tokenId)
    }

}
