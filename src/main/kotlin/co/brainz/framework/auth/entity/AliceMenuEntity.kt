package co.brainz.framework.auth.entity

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceMenuAuthMapEntity
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "awf_menu")
data class AliceMenuEntity(
        @Id
        val menuId: String,
        val pMenuId: String,
        val url: String,
        val sort: Int,
        val useYn: Boolean
): Serializable
