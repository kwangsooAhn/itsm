package co.brainz.framework.auth.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_menu")
data class AliceMenuEntity(
        @Id
        @Column(name = "menu_id", length = 100)
        val menuId: String,

        @Column(name = "p_menu_id", length = 100)
        val pMenuId: String,

        @Column(name = "url", length = 512)
        val url: String,

        @Column(name = "sort")
        val sort: Int,

        @Column(name = "use_yn")
        val useYn: Boolean
): Serializable
