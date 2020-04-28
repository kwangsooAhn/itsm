package co.brainz.itsm.numbering.controller

import co.brainz.itsm.numbering.service.NumberingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/numbering")
class NumberingRestController(private val numberingService: NumberingService) {

    @GetMapping("/{numberingId}")
    fun getNewNumbering(@PathVariable numberingId: String): String {
        return numberingService.getNewNumbering(numberingId)
    }
}
