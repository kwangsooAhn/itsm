package co.brainz.framework.auth.service

import co.brainz.framework.auth.entity.AliceIpVerificationEntity
import co.brainz.framework.auth.repository.AliceIpVerificationRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AliceIpVerificationService(private val aliceIpVerificationRepository: AliceIpVerificationRepository) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * IP 리스트를 조회한다.
     */
    fun getIpList(): MutableList<AliceIpVerificationEntity> {
        return aliceIpVerificationRepository.findAll()
    }
}
