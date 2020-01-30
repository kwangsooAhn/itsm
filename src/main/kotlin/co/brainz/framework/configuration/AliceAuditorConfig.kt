package co.brainz.framework.configuration

import co.brainz.framework.auditor.AliceAuditorAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

/**
 * JPA Auditor 설정 클래스.
 *
 */
@EnableJpaAuditing(auditorAwareRef = "aliceAuditorAware")
@Configuration
class AliceAuditorConfig {
    @Bean
    fun aliceAuditorAware(): AuditorAware<String> {
        return AliceAuditorAware()
    }
}