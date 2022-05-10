/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.documentStorage.service

import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceUtil
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.documentStorage.entity.DocumentStorageEntity
import co.brainz.itsm.documentStorage.entity.DocumentStoragePk
import co.brainz.itsm.documentStorage.repository.DocumentStorageRepository
import co.brainz.itsm.user.service.UserService
import co.brainz.workflow.instance.repository.WfInstanceRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DocumentStorageService(
    private val documentStorageRepository: DocumentStorageRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val wfInstanceRepository: WfInstanceRepository,
    private val userService: UserService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun insertDocumentStorage(instanceId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        try {
            val userEntity = userService.selectUserKey(currentSessionUser.getUserKey())
            val instanceEntity = wfInstanceRepository.findByInstanceId(instanceId)!!

            documentStorageRepository.save(
                DocumentStorageEntity(
                    user = userEntity,
                    instance = instanceEntity
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error(AliceUtil().printStackTraceToString(e))

            status = ZResponseConstants.STATUS.ERROR_FAIL
        } finally {
            return ZResponse(
                status = status.code
            )
        }
    }

    @Transactional
    fun deleteDocumentStorage(instanceId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        try {
            val userEntity = userService.selectUserKey(currentSessionUser.getUserKey())

            documentStorageRepository.deleteById(DocumentStoragePk(user = userEntity.userKey, instance = instanceId))
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error(AliceUtil().printStackTraceToString(e))

            status = ZResponseConstants.STATUS.ERROR_FAIL
        } finally {
            return ZResponse(
                status = status.code
            )
        }
    }
}
