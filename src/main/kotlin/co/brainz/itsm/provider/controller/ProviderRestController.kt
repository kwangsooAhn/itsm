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
        val instanceMap = LinkedHashMap<String, Any>()
        instanceMap["id"] = ""
        val processMap = LinkedHashMap<String, Any>()
        processMap["id"] = "abcdedfdddd"

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
        result["instance"] = instanceMap
        result["process"] = processMap
        result["token"] = tokenMap

        val jsonValue = Gson().toJson(result)

        tokenProvider.postToken(jsonValue)
    }

    @GetMapping("/put")
    fun putData() {
        val instanceMap = LinkedHashMap<String, Any>()
        instanceMap["id"] = "40288ab2707a9ccd01707a9cf6980000"
        val processMap = LinkedHashMap<String, Any>()
        processMap["id"] = "abcdedfdddd"

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
        tokenMap["id"] = "40288ab2707a9ccd01707a9cf6bc0001"
        tokenMap["isComplete"] = true
        tokenMap["elementId"] = ""
        tokenMap["assigneeId"] = "aaaaa"
        tokenMap["assigneeType"] = "bbbbbb"
        tokenMap["data"] = dataList

        val result = LinkedHashMap<String, Any>()
        result["instance"] = instanceMap
        result["process"] = processMap
        result["token"] = tokenMap

        val jsonValue = Gson().toJson(result)
        tokenProvider.putToken(jsonValue)
    }
}
