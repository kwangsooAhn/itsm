package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id
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
