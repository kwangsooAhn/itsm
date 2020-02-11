package co.brainz.framework.auditor

import co.brainz.itsm.utility.LocalDateTimeAttributeConverter
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

/**
 * 공통 Meta Data 클래스.
 *
 * 생성 사용자키, 생성일, 수정 사옹자키, 수정일을 공통으로 사용한다.
 *
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class AliceMetaEntity(
        @CreatedBy @Column(name="create_user_key", nullable = false, updatable = false)
        open var createUserKey: String = "",
        @Convert(converter = LocalDateTimeAttributeConverter::class)
        @CreatedDate @Column(name="create_dt", nullable = false, updatable = false)
        var createDt: LocalDateTime = LocalDateTime.now(),
        @LastModifiedBy @Column(name="update_user_key", insertable = false)
        var updateUserKey: String? = null,
        @Convert(converter = LocalDateTimeAttributeConverter::class)
        @LastModifiedDate @Column(name="update_dt", insertable = false)
        var updateDt: LocalDateTime? = LocalDateTime.now()

): Serializable
