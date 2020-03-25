package co.brainz.framework.auth.service

import co.brainz.framework.auth.entity.AliceAccessAllowEntity
import co.brainz.framework.auth.repository.AliceAccessAllowRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AliceAccessAllowService(private val aliceAccessAllowRepository: AliceAccessAllowRepository) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * IP 리스트를 조회한다.
     */
    fun getIpList(): MutableList<AliceAccessAllowEntity> {
        return aliceAccessAllowRepository.findAll()
    }
}