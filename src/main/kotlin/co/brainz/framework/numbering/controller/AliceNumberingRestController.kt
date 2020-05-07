package co.brainz.framework.numbering.controller

import co.brainz.framework.numbering.service.AliceNumberingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/numbering")
class AliceNumberingRestController(private val aliceNumberingService: AliceNumberingService) {

    @GetMapping("/{numberingId}")
    fun getNewNumbering(@PathVariable numberingId: String): String {
        return aliceNumberingService.getNewNumbering(numberingId)
    }
}
