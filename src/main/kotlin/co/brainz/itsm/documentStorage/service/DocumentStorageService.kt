/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.documentStorage.service

import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.documentStorage.dto.DocumentStorageRequestDto
import co.brainz.itsm.documentStorage.entity.DocumentStorageEntity
import co.brainz.itsm.documentStorage.entity.DocumentStoragePk
import co.brainz.itsm.documentStorage.repository.DocumentStorageRepository
import co.brainz.itsm.instance.service.InstanceService
import co.brainz.itsm.user.service.UserService
import co.brainz.workflow.instance.repository.WfInstanceRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DocumentStorageService(
    private val documentStorageRepository: DocumentStorageRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val instanceService: InstanceService,
    private val wfInstanceRepository: WfInstanceRepository,
    private val userService: UserService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun insertDocumentStorage(documentStorageRequestDto: DocumentStorageRequestDto): ZResponse {
        val status = ZResponseConstants.STATUS.SUCCESS
        val userEntity = userService.selectUserKey(currentSessionUser.getUserKey())
        val instanceId = documentStorageRequestDto.instanceId
        val documentId = documentStorageRequestDto.documentId

        if (instanceService.setInitInstance(instanceId, documentId)) {
            val instanceEntity = wfInstanceRepository.findByInstanceId(instanceId)!!
            documentStorageRepository.save(
                DocumentStorageEntity(
                    user = userEntity,
                    instance = instanceEntity
                )
            )
        }

        return ZResponse(
            status = status.code
        )

    }

    @Transactional
    fun deleteDocumentStorage(instanceId: String): ZResponse {
        val status = ZResponseConstants.STATUS.SUCCESS
        val userEntity = userService.selectUserKey(currentSessionUser.getUserKey())

        documentStorageRepository.deleteById(DocumentStoragePk(user = userEntity.userKey, instance = instanceId))

        return ZResponse(
            status = status.code
        )
    }

    fun getDocumentStorageDataExist(instanceId: String): ZResponse {
        val userEntity = userService.selectUserKey(currentSessionUser.getUserKey())
        val instanceEntity = wfInstanceRepository.findByInstanceId(instanceId)
        val isExistInstance: Boolean = if (instanceEntity == null) {
            false
        } else {
            documentStorageRepository.existsByInstanceAndUser(
                instanceEntity, userEntity
            )
        }

        return ZResponse(
            status = ZResponseConstants.STATUS.SUCCESS.code,
            message = "",
            data = isExistInstance
        )
    }
}
