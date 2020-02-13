package co.brainz.itsm.component.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/components")
class ComponentRestController() {

    @GetMapping("")
    fun getComponents() {

    }

}
