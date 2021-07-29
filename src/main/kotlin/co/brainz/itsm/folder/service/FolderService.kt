package co.brainz.itsm.folder.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.workflow.folder.service.WfFolderService
import co.brainz.workflow.provider.dto.RestTemplateFolderDto
import co.brainz.workflow.provider.dto.RestTemplateRelatedInstanceViewDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service

@Service
class FolderService(
    private val aliceUserRepository: AliceUserRepository,
    private val userDetailsService: AliceUserDetailsService,
    private val wfFolderService: WfFolderService,
    private val currentSessionUser: CurrentSessionUser
) {
    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * [tokenId]의 관련 문서 조회
     */
    fun getRelatedInstance(tokenId: String): List<RestTemplateRelatedInstanceViewDto>? {
        val relatedInstance = wfFolderService.getRelatedInstanceList(tokenId)
        val moreInfoAddRelatedInstance: MutableList<RestTemplateRelatedInstanceViewDto> = mutableListOf()
        relatedInstance.forEach {
            val user = aliceUserRepository.getOne(it.instanceCreateUserKey!!)
            val avatarPath = userDetailsService.makeAvatarPath(user)
            it.avatarPath = avatarPath
            moreInfoAddRelatedInstance.add(it)
        }
        return moreInfoAddRelatedInstance
    }

    fun getFolderId(tokenId: String): String? {
        return wfFolderService.getOriginFolder(tokenId).folderId
    }

    fun createFolder(restTemplateFolderDto: List<RestTemplateFolderDto>): Boolean {
        for (folder in restTemplateFolderDto) {
            folder.createUserKey = currentSessionUser.getUserKey()
        }
        return wfFolderService.createFolderData(restTemplateFolderDto)
    }

    fun deleteFolder(folderId: String, restTemplateFolderDto: RestTemplateFolderDto): Boolean {
        return wfFolderService.deleteFolderData(folderId, restTemplateFolderDto)
    }
}
