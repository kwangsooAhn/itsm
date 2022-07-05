
package co.brainz.itsm.resource.controller

import co.brainz.framework.fileTransaction.provider.AliceResourceProvider
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/resources")
class ResourceRestController(
    private val resourceProvider: AliceResourceProvider
) {
    /**
     * 전체 목록 가져오기.
     */
    /*@GetMapping("")
    fun getResourceAll(
        @RequestParam(value = "type", defaultValue = "") type: String,
        @RequestParam(value = "searchValue", defaultValue = "") searchValue: String,
        @RequestParam(value = "offset", defaultValue = "-1") offset: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(
            resourceProvider.getExternalResources(type, searchValue, offset.toInt())
        )
    }*/
}
