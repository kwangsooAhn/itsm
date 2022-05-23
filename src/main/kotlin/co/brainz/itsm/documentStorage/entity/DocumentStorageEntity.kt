/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.documentStorage.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_document_storage")
@IdClass(DocumentStoragePk::class)
data class DocumentStorageEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    val user: AliceUserEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id")
    val instance: WfInstanceEntity
) : Serializable

data class DocumentStoragePk(
    val user: String = "",
    val instance: String = ""
) : Serializable
