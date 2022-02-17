package co.brainz.itsm.plugin.entity

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "awf_plugin")
data class PluginEntity(
    val dd: String
) : Serializable
