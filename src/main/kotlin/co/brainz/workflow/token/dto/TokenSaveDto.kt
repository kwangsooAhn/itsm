package co.brainz.workflow.token.dto

import co.brainz.workflow.instance.dto.InstanceDto
import co.brainz.workflow.process.dto.ProcessDto
import java.io.Serializable

data class TokenSaveDto(
        val instanceDto: InstanceDto,
        val processDto: ProcessDto,
        val tokenDto: TokenDto
) : Serializable
