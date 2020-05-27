package co.brainz.workflow.engine.manager

import co.brainz.workflow.provider.dto.RestTemplateTokenDto

interface WfTokenManager {

    fun createToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto
    fun createNextToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto
    fun completeToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto

}
