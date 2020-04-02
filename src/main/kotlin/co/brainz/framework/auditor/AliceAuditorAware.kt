package co.brainz.framework.auditor

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.mapper.AliceUserAuthMapper
import co.brainz.framework.constants.AliceUserConstants
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.Optional

/**
 * JPA Auditor @CreatedBy, @LastModifiedBy 자동 설정 클래스
 *
 */
open class AliceAuditorAware: AuditorAware<AliceUserEntity> {
    @Override
    override fun getCurrentAuditor(): Optional<AliceUserEntity> {
        val userMapper: AliceUserAuthMapper = Mappers.getMapper(AliceUserAuthMapper::class.java)
        val attr = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val securityContext = attr.request.getSession(false)?.
                getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY) as SecurityContext?

        var aliceUserEntity = AliceUserEntity(userKey = AliceUserConstants.CREATE_USER_ID)

        if (securityContext != null) {
            aliceUserEntity = userMapper.toAliceUserEntity(securityContext.authentication.details as AliceUserDto)
        }
        return Optional.of(aliceUserEntity)
    }
}
