package co.brainz.framework.auditor

import co.brainz.framework.auth.entity.AliceUserEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener

/**
 * 공통 Meta Data 클래스.
 *
 * 생성 사용자키, 생성일, 수정 사옹자키, 수정일을 공통으로 사용한다.
 *
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class AliceMetaEntity(
    @CreatedBy
    @JoinColumn(name = "create_user_key", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    open var createUser: AliceUserEntity? = null,

    @CreatedDate
    @Column(name = "create_dt", nullable = false, updatable = false)
    open var createDt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedBy
    @JoinColumn(name = "update_user_key", insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    open var updateUser: AliceUserEntity? = null,

    @LastModifiedDate
    @Column(name = "update_dt", insertable = false)
    open var updateDt: LocalDateTime? = LocalDateTime.now()
) : Serializable
