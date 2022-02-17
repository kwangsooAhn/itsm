package co.brainz.itsm.plugin.entity

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "awf_plugin_history")
data class PluginHistoryEntity(
    val cc: String
) : Serializable
