package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_document_role_map")
@IdClass(AliceDocumentRoleMapPk::class)
data class AliceDocumentRoleMapEntity(
    @Id
    @Column(name = "document_id", length = 128)
    val documentId: String,

    @Column(name = "document_type", length = 100)
    val documentType: String,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    val role: AliceRoleEntity
) : Serializable

data class AliceDocumentRoleMapPk(
    val documentId: String = "",
    val role: String = ""
) : Serializable
