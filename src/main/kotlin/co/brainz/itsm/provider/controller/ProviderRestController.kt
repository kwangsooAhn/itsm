package co.brainz.itsm.provider.controller

import co.brainz.itsm.provider.TokenProvider
import com.google.gson.Gson
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/provider")
class ProviderRestController(private val tokenProvider: TokenProvider) {

    @GetMapping("/post")
    fun postData() {
        val documentMap = LinkedHashMap<String, Any>()
        documentMap["id"] = "32ds1261420w7edbcd5251d7b24a6c23"

        val dataList = mutableListOf<LinkedHashMap<String, Any>>()
        val data1 = LinkedHashMap<String, Any>()
        data1["componentId"] = "40288ab77066ce0a0170670cbb090004"
        data1["value"] = "xxxxxx"
        val data2 = LinkedHashMap<String, Any>()
        data2["componentId"] = "40288ab77066ce0a0170670cbb0c0005"
        data2["value"] = "ffffffffffffff"
        dataList.add(data1)
        dataList.add(data2)

        val tokenMap = LinkedHashMap<String, Any>()
        tokenMap["id"] = ""
        tokenMap["isComplete"] = true
        tokenMap["elementId"] = ""
        tokenMap["assigneeId"] = ""
        tokenMap["assigneeType"] = ""
        tokenMap["data"] = dataList

        val result = LinkedHashMap<String, Any>()
        result["document"] = documentMap
        result["token"] = tokenMap

        val jsonValue = Gson().toJson(result)
        tokenProvider.postTokenData(tokenProvider.makeTokenData(jsonValue))
    }

    @GetMapping("/put")
    fun putData() {
        val documentMap = LinkedHashMap<String, Any>()
        documentMap["id"] = ""

        val dataList = mutableListOf<LinkedHashMap<String, Any>>()
        val data1 = LinkedHashMap<String, Any>()
        data1["componentId"] = "40288ab77066ce0a0170670cbb090004"
        data1["value"] = "555555123123"
        val data2 = LinkedHashMap<String, Any>()
        data2["componentId"] = "40288ab77066ce0a0170670cbb0c0005"
        data2["value"] = "kkkk12123"
        dataList.add(data1)
        dataList.add(data2)

        val tokenMap = LinkedHashMap<String, Any>()
        tokenMap["id"] = "40288ab2707b109b01707b1103ca0001"
        tokenMap["isComplete"] = true
        tokenMap["elementId"] = ""
        tokenMap["assigneeId"] = "aaaaa"
        tokenMap["assigneeType"] = "bbbbbb"
        tokenMap["data"] = dataList

        val result = LinkedHashMap<String, Any>()
        result["document"] = documentMap
        result["token"] = tokenMap

        val jsonValue = Gson().toJson(result)
        tokenProvider.putTokenData(tokenProvider.makeTokenData(jsonValue))
    }
}
