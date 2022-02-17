package co.brainz.itsm.plugin.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_plugin")
data class PluginEntity(
    @Id
    @Column(name = "plugin_id", length = 100)
    val pluginId: String,

    @Column(name = "plugin_name", length = 100)
    val pluginName: String,

    @Column(name = "plugin_type", length = 100)
    val pluginType: String,

    @Column(name = "plugin_location", length = 1024)
    val pluginLocation: String,

    @Column(name = "plugin_command", length = 2048)
    val pluginCommand: String
) : Serializable
