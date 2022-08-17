/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.notification.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.notification.dto.NotificationConfigDetailDto
import co.brainz.framework.notification.dto.NotificationConfigDto
import co.brainz.framework.notification.dto.NotificationDto
import co.brainz.framework.notification.entity.NotificationEntity
import co.brainz.framework.notification.mapper.NotificationMapper
import co.brainz.framework.notification.repository.NotificationConfigDetailRepository
import co.brainz.framework.notification.repository.NotificationConfigRepository
import co.brainz.framework.notification.repository.NotificationRepository
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val userRepository: AliceUserRepository,
    private val notificationConfigRepository: NotificationConfigRepository,
    private val notificationConfigDetailRepository: NotificationConfigDetailRepository
) {
    @Value("\${notification.toast.enabled}")
    lateinit var toast: String

    @Value("\${notification.vendor.enabled}")
    lateinit var isVendorEnabled: String

    @Value("\${notification.vendor.target}")
    lateinit var vendorTarget: String

    private val notificationMapper: NotificationMapper = Mappers.getMapper(NotificationMapper::class.java)

    /**
     * 알림 리스트.
     */
    fun getNotificationList(): List<NotificationDto> {
        val userId = SecurityContextHolder.getContext().authentication.principal as String
        val notificationList = mutableListOf<NotificationDto>()
        // 알림 조회 - 문서제외
        notificationList.addAll(notificationRepository.findNotificationListExceptDocument(userId))
        // 알림 조회 - 문서용
        notificationList.addAll(notificationRepository.findNotificationListForDocument(userId))
        // sort
        notificationList.sortWith(compareBy<NotificationDto> { it.confirmYn }.thenByDescending { it.createDt })
        // 갯수 제한
        return notificationList.take(AliceConstants.NOTIFICATION_SIZE.toInt())
    }

    /**
     * 알림 Insert.
     */
    fun insertNotificationList(notificationDtoList: List<NotificationDto>) {
        val notificationEntityList = mutableListOf<NotificationEntity>()
        if (toast.toLowerCase() == "true") {
            notificationDtoList.forEach { notificationDto ->
                val userEntity = userRepository.findById(notificationDto.receivedUser).orElse(null)
                userEntity?.let {
                    val notificationEntity = notificationMapper.toNotificationEntity(notificationDto)
                    notificationEntity.receivedUser = userEntity
                    notificationEntityList.add(notificationEntity)

                    // Insert Notification Data For Vendor
                    if (isVendorEnabled == "true" && vendorTarget.isNotBlank()) {
                        val notificationEntity = notificationMapper.toNotificationEntity(notificationDto)
                        notificationEntity.receivedUser = userEntity
                        notificationEntity.target = vendorTarget
                        notificationEntityList.add(notificationEntity)
                    }
                }
            }

            notificationRepository.saveAll(notificationEntityList)
        }
    }

    /**
     * 알림 confirm update.
     */
    fun updateNotification(notificationId: String, target: String) {
        val notificationEntity = notificationRepository.findById(notificationId).orElse(null)
        notificationEntity?.let {
            when (target) {
                "confirm" -> notificationEntity.confirmYn = true
                "display" -> notificationEntity.displayYn = true
            }
            notificationRepository.save(notificationEntity)
        }
    }

    /**
     * 알림 delete.
     */
    fun deleteNotification(notificationId: String) {
        notificationRepository.deleteById(notificationId)
    }

    /**
     *  알람 발송 관리 설정정보 조회
     */
    fun getNotificationConfig(): List<NotificationConfigDto> {
        val notificationConfigs = mutableListOf<NotificationConfigDto>()

        // 알람 타입 조회 ex> 신청서/ CMDB 라이센스
        notificationConfigRepository.findAll().forEach { notificationConfig ->
            val notificationConfigDto = NotificationConfigDto(
                notificationCode = notificationConfig.notificationCode,
                notificationName = notificationConfig.notificationName
            )
            // 알람 타입별 채널 상세정보 DTO에 담기
            val notificationConfigDetails =  mutableListOf<NotificationConfigDetailDto>()
            notificationConfig.notificationConfigDetails.forEach { configDetail ->
                notificationConfigDetails.add(
                    NotificationConfigDetailDto(
                        channel = configDetail.channel,
                        useYn = configDetail.useYn,
                        configDetail = configDetail.configDetail
                    )
                )
            }
            notificationConfigDto.notificationConfigDetails = notificationConfigDetails
            // toast -> sms -> mail 순서로 정렬
            notificationConfigDto.notificationConfigDetails?.sortByDescending { it.channel }
            notificationConfigs.add(notificationConfigDto)
        }

        return notificationConfigs
    }

    /**
     *  알람 발송 관리 설정정보 업데이트
     */
    @Transactional
    fun updateNotificationConfig(config: NotificationConfigDto): ZResponse {
        var result = ZResponseConstants.STATUS.SUCCESS
        try {
            notificationConfigRepository.findByNotificationCode(config.notificationCode)
                .notificationConfigDetails
                .sortedByDescending { it.channel } // toast -> sms -> mail 순서로 정렬
                .forEachIndexed { index, configDetail ->
                    configDetail.useYn = config.notificationConfigDetails!![index].useYn
                    configDetail.configDetail = config.notificationConfigDetails!![index].configDetail
                }
        } catch (e: Exception) {
            e.printStackTrace()
            result = ZResponseConstants.STATUS.ERROR_FAIL
        } finally {
            return ZResponse(
                status = result.code
            )
        }
    }
}
