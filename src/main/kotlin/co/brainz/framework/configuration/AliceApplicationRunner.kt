package co.brainz.framework.configuration

import co.brainz.framework.auth.entity.AliceUrlEntity
import co.brainz.framework.auth.repository.AliceUrlRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * 부팅시 최초 1회에 한해 실행되는 기능을 추가할 수 있는 모듈.
 */
@Order(1)
@Component
class AliceApplicationRunner(private val aliceUrlRepository: AliceUrlRepository): ApplicationRunner {

    companion object {
        var aliceUrls: List<AliceUrlEntity> = emptyList()
    }

    override fun run(args: ApplicationArguments?) {
        aliceUrls = getUrlAuthList()
    }

    fun getUrlAuthList(): List<AliceUrlEntity> {
        return aliceUrlRepository.findAliceUrlEntityByRequiredAuthIs(false)
    }
}
