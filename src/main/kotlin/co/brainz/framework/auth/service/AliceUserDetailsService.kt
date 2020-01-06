package co.brainz.framework.auth.service

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.repository.AliceAuthRepository
import co.brainz.framework.auth.repository.AliceUserRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Component

@Component
class AliceUserDetailsService(private var aliceUserRepository: AliceUserRepository
                              , private var aliceAuthRepository: AliceAuthRepository) {

    @Throws(EmptyResultDataAccessException::class)
    fun loadUserByUsername(userId: String): AliceUserEntity {
        return aliceUserRepository.findByUserId(userId)
    }

    fun getAuthList(authIds: MutableSet<String>): Set<AliceAuthEntity> {
        return aliceAuthRepository.findByAuthIdIn(authIds)
    }

    @Throws(EmptyResultDataAccessException::class)
    fun loadUserByUserIdAndPlatform(userId: String, platform: String): AliceUserEntity {
        return aliceUserRepository.findByUserIdAndPlatform(userId, platform)
    }

}