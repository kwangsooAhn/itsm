package co.brainz.workflow.token.controller

import co.brainz.workflow.engine.WFEngine
import co.brainz.workflow.instance.dto.InstanceDto
import co.brainz.workflow.token.constants.TokenConstants
import co.brainz.workflow.token.dto.TokenDataDto
import co.brainz.workflow.token.dto.TokenDto
import co.brainz.workflow.token.dto.TokenSaveDto
import co.brainz.workflow.token.entity.TokenDataEntity
import com.google.gson.Gson
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional

@RestController
@RequestMapping("/rest/wf/tokens")
class WFTokenRestController(private val wfEngine: WFEngine) {

    //@Transactional
    //@GetMapping("")
    fun postToken() {
        val instanceMap = LinkedHashMap<String, Any>()
        instanceMap["id"] = ""
        val processMap = LinkedHashMap<String, Any>()
        processMap["id"] = "abcdedfdddd"

        val dataList = mutableListOf<LinkedHashMap<String, Any>>()
        val data1 = LinkedHashMap<String, Any>()
        data1["compId"] = "40288ab77066ce0a0170670cbb090004"
        data1["value"] = "xxxxxx"
        val data2 = LinkedHashMap<String, Any>()
        data2["compId"] = "40288ab77066ce0a0170670cbb0c0005"
        data2["value"] = "ffffffffffffff"
        dataList.add(data1)
        dataList.add(data2)

        val tokenMap = LinkedHashMap<String, Any>()
        tokenMap["id"] = ""
        tokenMap["isComplete"] = true
        tokenMap["elemId"] = ""
        tokenMap["assigneeId"] = ""
        tokenMap["assigneeType"] = ""
        tokenMap["data"] = dataList

        val result = LinkedHashMap<String, Any>()
        result["instance"] = instanceMap
        result["process"] = processMap
        result["token"] = tokenMap

        val jsonValue = Gson().toJson(result)
        println(">>>>>>>>>>>>")
        println(jsonValue)

        wfEngine.token().postToken(jsonValue)
    }

    @Transactional
    @GetMapping("")
    fun putToken() {
        val instanceMap = LinkedHashMap<String, Any>()
        instanceMap["id"] = "40288ab27076642f017076648e5b0000"
        val processMap = LinkedHashMap<String, Any>()
        processMap["id"] = "abcdedfdddd"

        val dataList = mutableListOf<LinkedHashMap<String, Any>>()
        val data1 = LinkedHashMap<String, Any>()
        data1["compId"] = "40288ab77066ce0a0170670cbb090004"
        data1["value"] = "555555"
        val data2 = LinkedHashMap<String, Any>()
        data2["compId"] = "40288ab77066ce0a0170670cbb0c0005"
        data2["value"] = "kkkk"
        dataList.add(data1)
        dataList.add(data2)

        val tokenMap = LinkedHashMap<String, Any>()
        tokenMap["id"] = "40288ab27076642f017076648e870001"
        tokenMap["isComplete"] = true
        tokenMap["elemId"] = ""
        tokenMap["assigneeId"] = "a"
        tokenMap["assigneeType"] = "b"
        tokenMap["data"] = dataList

        val result = LinkedHashMap<String, Any>()
        result["instance"] = instanceMap
        result["process"] = processMap
        result["token"] = tokenMap

        val jsonValue = Gson().toJson(result)
        println(">>>>>>>>>>>>")
        println(jsonValue)

        wfEngine.token().putToken(jsonValue)
    }

/*    @GetMapping("/create")
    fun createToken() {

        //가데이터를 만든다.
        val tokenDto = TokenDto(
                tokenId = "",
                tokenStatus = TokenConstants.Status.RUNNING.code,
                elemId = "111",
                instId = "40288ab27066b7f2017066b839d80000"
        )

        val tokenDataDtoList = mutableListOf<TokenDataDto>()
        val tokenDataDto = TokenDataDto(
                tokenId = "",
                instId = "40288ab27066b7f2017066b839d80000",
                compId = "40288ab77066ce0a0170670cbb090004",
                value = "test123123123"
        )
        tokenDataDtoList.add(tokenDataDto)

        val tokenSaveDto = TokenSaveDto(
                tokenDto = tokenDto,
                tokenDataDtoList = tokenDataDtoList
        )

        wfEngine.token().createToken(tokenDto)

    }

    @GetMapping("/complete/{tokenId}")
    fun completeInstance(@PathVariable tokenId: String) {
        val tokenDto = TokenDto(
                tokenId = tokenId,
                tokenStatus = TokenConstants.Status.RUNNING.code,
                elemId = "111",
                instId = "40288ab27066b7f2017066b839d80000"
        )
        wfEngine.token().completeToken(tokenDto)
    }

    @GetMapping("/data")
    fun saveToken() {
        val tokenDto = TokenDto(
                tokenId = "",
                tokenStatus = TokenConstants.Status.RUNNING.code,
                elemId = "111",
                instId = "40288ab27066b7f2017066b839d80000"
        )

        val tokenDataDtoList = mutableListOf<TokenDataDto>()
        val tokenDataDto1 = TokenDataDto(
                tokenId = "",
                instId = "40288ab27066b7f2017066b839d80000",
                compId = "40288ab77066ce0a0170670cbb090004",
                value = "xxxx1111"
        )
        val tokenDataDto2 = TokenDataDto(
                tokenId = "",
                instId = "40288ab27066b7f2017066b839d80000",
                compId = "40288ab77066ce0a0170670cbb0c0005",
                value = "xxxx2222"
        )
        tokenDataDtoList.add(tokenDataDto1)
        tokenDataDtoList.add(tokenDataDto2)

        val tokenSaveDto = TokenSaveDto(
                tokenDto = tokenDto,
                tokenDataDtoList = tokenDataDtoList
        )

        println(">>>>>>>>>>>>>>>>>>>>>>>>")
        println(">>>>>>>>>>>>>>>>>>>>>>>>")

        //wfEngine.token().executeToken(tokenSaveDto)
    }

    @GetMapping("/data/{tokenId}")
    @Transactional
    fun updateToken(@PathVariable tokenId: String) {
        val tokenDto = TokenDto(
                isComplete = false,
                tokenId = tokenId,
                tokenStatus = TokenConstants.Status.RUNNING.code,
                elemId = "111",
                instId = "40288ab27066b7f2017066b839d80000"
        )

        val tokenDataDtoList = mutableListOf<TokenDataDto>()
        val tokenDataDto1 = TokenDataDto(
                tokenId = tokenId,
                instId = "40288ab27066b7f2017066b839d80000",
                compId = "40288ab77066ce0a0170670cbb090004",
                value = "ddddddd"
        )
        val tokenDataDto2 = TokenDataDto(
                tokenId = tokenId,
                instId = "40288ab27066b7f2017066b839d80000",
                compId = "40288ab77066ce0a0170670cbb0c0005",
                value = "ffffffffff"
        )
        tokenDataDtoList.add(tokenDataDto1)
        tokenDataDtoList.add(tokenDataDto2)

        val tokenSaveDto = TokenSaveDto(
                tokenDto = tokenDto,
                tokenDataDtoList = tokenDataDtoList
        )
        wfEngine.token().executeToken(tokenSaveDto)
    }*/

}
