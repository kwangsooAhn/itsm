package co.brainz.framework.notification.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.notification.dto.NotificationDto
import co.brainz.framework.notification.entity.NotificationEntity
import co.brainz.framework.notification.mapper.NotificationMapper
import co.brainz.framework.notification.repository.NotificationRepository
import org.mapstruct.factory.Mappers
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val userRepository: AliceUserRepository
) {

    private val notificationMapper: NotificationMapper = Mappers.getMapper(NotificationMapper::class.java)

    /**
     * 알림 리스트.
     */
    fun getNotificationList(): MutableList<NotificationDto> {
        val userId = SecurityContextHolder.getContext().authentication.principal as String
        val userEntity = userRepository.findByUserId(userId)
        val notificationList = mutableListOf<NotificationDto>()
        val notificationEntityList = notificationRepository.findNotificationList(userEntity)
        notificationEntityList.forEach {
            notificationList.add(notificationMapper.toNotificationDto(it))
        }
        return notificationList
    }

    /**
     * 알림 Insert.
     */
    fun insertNotificationList(notificationDtoList: List<NotificationDto>) {
        val notificationEntityList = mutableListOf<NotificationEntity>()
        notificationDtoList.forEach { notificationDto ->
            val userEntity = userRepository.findById(notificationDto.receivedUser).orElse(null)
            userEntity?.let {
                val notificationEntity = notificationMapper.toNotificationEntity(notificationDto)
                notificationEntity.receivedUser = userEntity
                notificationEntityList.add(notificationEntity)
            }
        }
        notificationRepository.saveAll(notificationEntityList)
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
}
