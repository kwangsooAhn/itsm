package co.brainz.framework.auditor

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import java.util.Optional
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * JPA Auditor @CreatedBy, @LastModifiedBy 자동 설정 클래스
 *
 */
open class AliceAuditorAware: AuditorAware<AliceUserEntity> {
    @Override
    override fun getCurrentAuditor(): Optional<AliceUserEntity> {
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val securityContext = attr.request.getSession(false)?.
                getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY) as SecurityContext?

        if (securityContext != null) {
            //val aliceUserDto = securityContext.authentication.details as AliceUserDto
            val aliceUserEntity = securityContext.authentication.details as AliceUserEntity
            //return Optional.of(aliceUserDto.userKey)
            return Optional.of(aliceUserEntity)
        }
        return Optional.of(AliceUserEntity("","","","","",true,0,null,null,null,null,"",null,"", LocalDateTime.now(),"","","",""))
    }
}