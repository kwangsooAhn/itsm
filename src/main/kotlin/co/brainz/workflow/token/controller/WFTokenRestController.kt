package co.brainz.workflow.token.controller

import co.brainz.workflow.engine.WFEngine
import co.brainz.workflow.token.dto.TokenSaveDto
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional

@RestController
@RequestMapping("/rest/wf/tokens")
class WFTokenRestController(private val wfEngine: WFEngine) {

    @Transactional
    @PostMapping("")
    fun postToken(@RequestBody tokenSaveDto: TokenSaveDto) {
        wfEngine.token().postToken(tokenSaveDto)
    }

    @Transactional
    @PutMapping("/{tokenId}")
    fun putToken(@RequestBody tokenSaveDto: TokenSaveDto, @PathVariable tokenId: String) {
        wfEngine.token().putToken(tokenSaveDto)
    }

}
