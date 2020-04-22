package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_menu_auth_map")
@IdClass(AliceMenuAuthMapPk::class)
data class AliceMenuAuthMapEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    val menu: AliceMenuEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id")
    val auth: AliceAuthEntity
) : Serializable

data class AliceMenuAuthMapPk(
    val menu: String = "",
    val auth: String = ""
) : Serializable
